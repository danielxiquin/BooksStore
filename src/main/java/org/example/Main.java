package org.example;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class BTreeNode{
    List<JSONObject> books;
    List<BTreeNode> children;
    boolean isLeaf;

    public BTreeNode(boolean isLeaf){
        this.books = new ArrayList<>();
        this.children = new ArrayList<>();
        this.isLeaf = isLeaf;
    }

    //Metodo de incertar
    public void insertNonFull(JSONObject book) {
        int i = this.books.size() - 1;

        if (this.isLeaf) {
            while (i >= 0 && this.books.get(i).getString("isbn").compareTo(book.getString("isbn")) > 0) {
                i--;
            }
            this.books.add(i + 1, book);
        } else {
            while (i >= 0 && this.books.get(i).getString("isbn").compareTo(book.getString("isbn")) > 0) {
                i--;
            }

            if (this.children.get(i + 1).books.size() == 4) {
                this.splitChild(i + 1, this.children.get(i + 1));
                if (this.books.get(i + 1).getString("isbn").compareTo(book.getString("isbn")) < 0) {
                    i++;
                }
            }
            this.children.get(i + 1).insertNonFull(book);
        }
    }

    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.isLeaf);
        z.books.addAll(y.books.subList(2, y.books.size()));
        y.books.subList(2, y.books.size()).clear();

        if (!y.isLeaf) {
            z.children.addAll(y.children.subList(2, y.children.size()));
            y.children.subList(2, y.children.size()).clear();
        }

        this.children.add(i + 1, z);
        this.books.add(i, y.books.remove(1));
    }

    //Metodo de actualización
    public int findkey(String isbn){
        for(int idx = 0; idx < books.size(); idx++){
            if(books.get(idx) != null && books.get(idx).getString("isbn").compareTo(isbn)== 0)     {
                return idx;
            }
        }
        return books.size();
    }

    public boolean updateBook(String isbn, Map<String, Object> updateData){
        int idx = findkey(isbn);

        if(idx < books.size()){
            JSONObject book = books.get(idx);

            for(Map.Entry<String, Object> entry : updateData.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                book.put(key, value);
            }
            return true;
        }

        if(isLeaf){
            if(idx < children.size()){
                return children.get(idx).updateBook(isbn, updateData);
            }
        }
        return false;
    }

    //Metodo de eliminacion
    public void removeBook(String isbn){

        int idx = findkey(isbn);

        if(idx < books.size() && books.get(idx).getString("isbn").compareTo(isbn) == 0){
            if(isLeaf){
                books.remove((idx));
            }else{
                removeFromNonLeaf(idx);
            }
        }else{
            if(isLeaf){
                return;
            }

            boolean flag = (idx == books.size());

            if(children.get(idx).books.size() < 2){
                fill(idx);
            }

            if(flag && idx > books.size()){
                children.get(idx-1).removeBook(isbn);
            }else{
                children.get(idx).removeBook(isbn);
            }

        }
    }

    public void fill(int idx) {
        if (idx != 0 && children.get(idx - 1).books.size() >= 2) {
            borrowFromPrev(idx);
        } else if (idx != books.size() && children.get(idx + 1).books.size() >= 2) {
            borrowFromNext(idx);
        } else {
            if (idx != books.size()) {
                merge(idx);
            } else {
                merge(idx - 1);
            }
        }
    }

    public void removeFromNonLeaf(int idx) {
        String isbnToRemove = books.get(idx).getString("isbn");

        if (children.get(idx).books.size() >= 2) {
            JSONObject pred = getPredecessor(idx);
            books.set(idx, pred);
            children.get(idx).removeBook(pred.getString("isbn"));
        }
        else if (children.get(idx + 1).books.size() >= 2) {
            JSONObject succ = getSuccessor(idx);
            books.set(idx, succ);
            children.get(idx + 1).removeBook(succ.getString("isbn"));
        }

        else {
            merge(idx);
            children.get(idx).removeBook(isbnToRemove);
        }
    }

    public void borrowFromPrev(int idx) {
        BTreeNode child = children.get(idx);
        BTreeNode sibling = children.get(idx - 1);

        child.books.add(0, books.get(idx - 1));

        books.set(idx - 1, sibling.books.remove(sibling.books.size() - 1));

        if (!child.isLeaf) {
            child.children.add(0, sibling.children.remove(sibling.children.size() - 1));
        }
    }

    public void borrowFromNext(int idx) {
        BTreeNode child = children.get(idx);
        BTreeNode sibling = children.get(idx + 1);

        child.books.add(books.get(idx));

        books.set(idx, sibling.books.remove(0));

        if (!child.isLeaf) {
            child.children.add(sibling.children.remove(0));
        }
    }

    public void merge(int idx) {
        BTreeNode child = children.get(idx);
        BTreeNode sibling = children.get(idx + 1);

        child.books.add(books.remove(idx));

        child.books.addAll(sibling.books);

        if (!child.isLeaf) {
            child.children.addAll(sibling.children);
        }

        children.remove(idx + 1);
    }

    public JSONObject getPredecessor(int idx) {
        BTreeNode current = children.get(idx);
        while (!current.isLeaf) {
            current = current.children.get(current.books.size());
        }
        return current.books.get(current.books.size() - 1);
    }

    public JSONObject getSuccessor(int idx) {
        BTreeNode current = children.get(idx + 1);
        while (!current.isLeaf) {
            current = current.children.get(0);
        }
        return current.books.get(0);
    }

    //Metodo de buscar ya no lo estoy usando a ver si sale abajo
/*
    public boolean searchByName(String name, Map<String, Object> bookData) {
        int idx = -1;
        int low = 0;
        int high = books.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            String midName = books.get(mid).getString("name");

            if (midName.equals(name)) {
                idx = mid;
                break;
            } else if (midName.compareTo(name) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (idx >= 0) {
            JSONObject book = books.get(idx);
            for (String key : book.keySet()) {
                bookData.put(key, book.get(key));
            }
            return true;
        }

        if (isLeaf) {
            return false;
        }

        for (BTreeNode child : children) {
            if (child.searchByName(name, bookData)) {
                return true;
            }
        }

        return false;
    }
*/

    public JSONObject findBookByIsbn(String isbn) {

        for (JSONObject book : books) {
            if (book.getString("isbn").equals(isbn)) {
                return book;
            }
        }

        if (isLeaf) {
            return null;
        }

        int i = 0;
        while (i < books.size() && isbn.compareTo(books.get(i).getString("isbn")) > 0) {
            i++;
        }

        return children.get(i).findBookByIsbn(isbn);
    }
}

class BTree{
    BTreeNode root;
    int t;
    HashMap<String, List<JSONObject>> bookIndexByName;


    public BTree(){
        this.t = 5;
        this.root = null;
        this.bookIndexByName = new HashMap<>();
    }

    public void insert(JSONObject book) {
        String name = book.getString("name");
        bookIndexByName.putIfAbsent(name, new ArrayList<>());
        bookIndexByName.get(name).add(book);

        if (root == null) {
            root = new BTreeNode(true);
            root.books.add(book);
        } else {
            if (root.books.size() == 4) {
                BTreeNode newNode = new BTreeNode(false);
                newNode.children.add(root);
                newNode.splitChild(0, root);
                root = newNode;
            }
            root.insertNonFull(book);
        }
    }

    public boolean updateBook(String isbn, Map<String, Object> updateData) {
        if (root != null) {
            boolean updated = root.updateBook(isbn, updateData);

            if (updated) {

                JSONObject updatedBook = findBookByIsbn(isbn);

                if (updatedBook != null) {
                    String newName = updatedBook.getString("name");


                    List<JSONObject> booksByName = bookIndexByName.get(newName);
                    if (booksByName != null) {
                        booksByName.removeIf(book -> book.getString("isbn").equals(isbn));
                        booksByName.add(updatedBook);
                    }
                }
            }

            return updated;
        }
        return false;
    }



    public void removeBook(String isbn) {
        if (root != null) {
            JSONObject bookToRemove = findBookByIsbn(isbn);

            if (bookToRemove != null) {
                String name = bookToRemove.getString("name");
                List<JSONObject> booksByName = bookIndexByName.get(name);

                if (booksByName != null) {
                    booksByName.remove(bookToRemove);
                    if (booksByName.isEmpty()) {
                        bookIndexByName.remove(name);
                    }
                }
            }

            root.removeBook(isbn);
            if (root.books.size() == 0) {
                if (root.isLeaf) {
                    root = null;
                } else {
                    root = root.children.get(0);
                }
            }
        }
    }



    public JSONObject searchByName(String name) {
        List<JSONObject> books = bookIndexByName.get(name);
        if (books != null && !books.isEmpty()) {
            return books.get(0);
        }
        return null;
    }


    public JSONObject findBookByIsbn(String isbn) {
        if (root != null) {
            return root.findBookByIsbn(isbn);
        }
        return null;
    }


}

public class Main{
    public static void ReaderCSV(String file, BTree tree){
        String filePath = file;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;


            while ((line = reader.readLine()) != null) {

                int separatorIndex = line.indexOf(';');
                if (separatorIndex == -1) {
                    System.err.println("Formato incorrecto: " + line);
                    continue;
                }

                String operation = line.substring(0, separatorIndex).trim();
                String jsonData = line.substring(separatorIndex + 1).trim();


                try{
                    JSONObject jsonObject = new JSONObject(jsonData);

                    switch (operation){
                        case "INSERT":
                            tree.insert(jsonObject);

                            break;
                        case "PATCH":
                            String isbn = jsonObject.getString("isbn");
                            Map<String, Object> updateData = jsonObject.toMap();
                            updateData.remove("isbn");
                            boolean update = tree.updateBook(isbn, updateData);

                            break;

                        case "DELETE":
                            String isb = jsonObject.getString("isbn");
                            tree.removeBook(isb);
                            break;
                        default:
                            System.err.println("Operación desconocida: " + operation);
                    }

                } catch (Exception e){

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void Exit(String file, BTree tree) {
        String filepath = file;

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath));
             BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {

            String line;

            while ((line = reader.readLine()) != null) {
                int separatorIndex = line.indexOf(';');
                if (separatorIndex == -1) {
                    System.err.println("Formato incorrecto: " + line);
                    continue;
                }

                String operation = line.substring(0, separatorIndex).trim();
                String jsonData = line.substring(separatorIndex + 1).trim();

                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String name = jsonObject.getString("name");

                    JSONObject foundBook = tree.searchByName(name);

                    if (foundBook != null) {
                        String formattedOutput = String.format(
                                "{\"isbn\":\"%s\",\"name\":\"%s\",\"author\":\"%s\",\"price\":\"%s\",\"quantity\":\"%s\"}",
                                foundBook.getString("isbn"),
                                foundBook.getString("name"),
                                foundBook.getString("author"),
                                foundBook.getString("price"),
                                foundBook.getString("quantity")
                        );
                        writer.write(formattedOutput);
                        writer.newLine();
                    } else {
                        System.out.println("Libro no encontrado: " + name);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args){
        String file = "lab01_books.csv";
        String file2 = "lab01_searchs.csv";
        BTree tree = new BTree();
        //Insertador, actualizando y eliminando libris en el arbol
        ReaderCSV(file, tree);
        //Despues de haber insertado, actulizado y eliminado libros en el arbol
        Exit(file2, tree);



    }
}