
class Node {
    ParkingLot element;
    int h;  // Yükseklik
    Node leftChild;
    Node rightChild;

    // Varsayılan yapıcı
    public Node() {
        leftChild = null;
        rightChild = null;
        element = null;
        h = 0;
    }

    // Parametreli yapıcı
    public Node(ParkingLot element) {
        leftChild = null;
        rightChild = null;
        this.element = element;
        h = 0;
    }
}

// AVL ağacı için sınıf
class AVLTree {
    private Node rootNode;

    // Yapıcı
    public AVLTree() {
        rootNode = null;
    }

    // Eleman ekleme
    public void insert(ParkingLot element) {
        rootNode = insert(element, rootNode);
    }

    // Yükseklik alma
    private int getHeight(Node node) {
        return node == null ? -1 : node.h;
    }

    // Maksimum yükseklik alma
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight) {
        return Math.max(leftNodeHeight, rightNodeHeight);
    }

    // Eleman ekleme (rekürsif)
    private Node insert(ParkingLot element, Node node) {
        // Düğüm null ise yeni düğüm oluştur
        if (node == null) {
            node = new Node(element);
        } else if (element.getCapacity() < node.element.getCapacity()) {
            node.leftChild = insert(element, node.leftChild);
            if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2) {
                if (element.getCapacity() < node.leftChild.element.getCapacity()) {
                    node = rotateWithLeftChild(node);
                } else {
                    node = doubleWithLeftChild(node);
                }
            }
        } else if (element.getCapacity() > node.element.getCapacity()) {
            node.rightChild = insert(element, node.rightChild);
            if (getHeight(node.rightChild) - getHeight(node.leftChild) == 2) {
                if (element.getCapacity() > node.rightChild.element.getCapacity()) {
                    node = rotateWithRightChild(node);
                } else {
                    node = doubleWithRightChild(node);
                }
            }
        } // Eğer aynı kapasite varsa, ekleme yapılmaz.

        node.h = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        return node;
    }
    public void delete(int capacity) {
        rootNode = delete(capacity, rootNode);
    }

    // Eleman silme (rekürsif)
    private Node delete(int capacity, Node node) {
        // Eğer düğüm null ise, bu capacity yok demektir
        if (node == null) {
            return null;
        }

        // Hedef düğümü bulma
        if (capacity < node.element.getCapacity()) {
            node.leftChild = delete(capacity, node.leftChild);
        } else if (capacity > node.element.getCapacity()) {
            node.rightChild = delete(capacity, node.rightChild);
        } else {
            // Düğüm bulundu, silme işlemi
            if (node.leftChild == null && node.rightChild == null) {
                // Hiç çocuk yoksa, bu düğüm silinebilir
                node = null;
            } else if (node.leftChild == null) {
                // Tek bir sağ çocuk varsa
                node = node.rightChild;
            } else if (node.rightChild == null) {
                // Tek bir sol çocuk varsa
                node = node.leftChild;
            } else {
                // İki çocuk varsa, in-order predecessor veya successor kullan
                ParkingLot predecessor = findMax(node.leftChild);
                node.element = predecessor; // Değeri kopyala
                node.leftChild = delete(predecessor.getCapacity(), node.leftChild); // Önceki düğümü sil
            }
        }

        // Yüksekliği güncelle
        if (node != null) {
            node.h = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        }

        // AVL dengesini kontrol et ve gerekli döndürmeleri yap
        return balance(node);
    }
    private ParkingLot findMax(Node node) {
        while (node.rightChild != null) {
            node = node.rightChild;
        }
        return node.element;
    }

    // Dengeleme metodu
    private Node balance(Node node) {
        if (node == null) {
            return null;
        }

        // Sol alt ağacın yüksekliği
        if (getHeight(node.leftChild) - getHeight(node.rightChild) == 2) {
            if (getHeight(node.leftChild.leftChild) >= getHeight(node.leftChild.rightChild)) {
                node = rotateWithLeftChild(node);
            } else {
                node = doubleWithLeftChild(node);
            }
        }
        // Sağ alt ağacın yüksekliği
        else if (getHeight(node.rightChild) - getHeight(node.leftChild) == 2) {
            if (getHeight(node.rightChild.rightChild) >= getHeight(node.rightChild.leftChild)) {
                node = rotateWithRightChild(node);
            } else {
                node = doubleWithRightChild(node);
            }
        }

        return node;
    }

    // Sol çocuk ile döndürme
    private Node rotateWithLeftChild(Node node2) {
        Node node1 = node2.leftChild;
        node2.leftChild = node1.rightChild;
        node1.rightChild = node2;
        node2.h = getMaxHeight(getHeight(node2.leftChild), getHeight(node2.rightChild)) + 1;
        node1.h = getMaxHeight(getHeight(node1.leftChild), node2.h) + 1;
        return node1;
    }

    // Sağ çocuk ile döndürme
    private Node rotateWithRightChild(Node node1) {
        Node node2 = node1.rightChild;
        node1.rightChild = node2.leftChild;
        node2.leftChild = node1;
        node1.h = getMaxHeight(getHeight(node1.leftChild), getHeight(node1.rightChild)) + 1;
        node2.h = getMaxHeight(getHeight(node2.rightChild), node1.h) + 1;
        return node2;
    }

    // Çift döndürme (sol çocuk)
    private Node doubleWithLeftChild(Node node3) {
        node3.leftChild = rotateWithRightChild(node3.leftChild);
        return rotateWithLeftChild(node3);
    }

    // Çift döndürme (sağ çocuk)
    private Node doubleWithRightChild(Node node1) {
        node1.rightChild = rotateWithLeftChild(node1.rightChild);
        return rotateWithRightChild(node1);
    }

    // Toplam düğüm sayısını alma
    public int getTotalNumberOfNodes() {
        return getTotalNumberOfNodes(rootNode);
    }

    private int getTotalNumberOfNodes(Node head) {
        if (head == null) {
            return 0;
        } else {
            return 1 + getTotalNumberOfNodes(head.leftChild) + getTotalNumberOfNodes(head.rightChild);
        }
    }

    // Eleman arama
    public ParkingLot search(int capacity) {
        return search(rootNode, capacity);
    }

    private ParkingLot search(Node head, int capacity) {
        if (head == null) {
            return null; // Eğer düğüm null ise, null döndür
        }
        int headElementCapacity = head.element.getCapacity();
        if (capacity < headElementCapacity) {
            return search(head.leftChild, capacity); // Sol alt ağa git
        } else if (capacity > headElementCapacity) {
            return search(head.rightChild, capacity); // Sağ alt ağa git
        } else {
            return head.element; // Düğüm bulundu, elementi döndür
        }
    }

    // In-order traversali
    public void inorderTraversal() {
        inorderTraversal(rootNode);
    }

    private void inorderTraversal(Node head) {
        if (head != null) {
            inorderTraversal(head.leftChild);
            System.out.print(head.element.getCapacity() + " ");
            inorderTraversal(head.rightChild);
        }
    }

    // Pre-order traversali
    public void preorderTraversal() {
        preorderTraversal(rootNode);
    }

    private void preorderTraversal(Node head) {
        if (head != null) {
            System.out.print(head.element.getCapacity() + " ");
            preorderTraversal(head.leftChild);
            preorderTraversal(head.rightChild);
        }
    }

    public void postorderTraversal() {
        postorderTraversal(rootNode);
    }

    private void postorderTraversal(Node head) {
        if (head != null) {
            postorderTraversal(head.leftChild);
            postorderTraversal(head.rightChild);
            System.out.print(head.element.getCapacity() + " ");
        }
    }

    // In-order predecessor bulma
    public ParkingLot findInOrderPredecessor(ParkingLot parkingLot) {
        Node predecessor = null;
        Node currentNode = rootNode;

        // İlk olarak, parkingLot düğümünü ve in-order öncülünü bulmaya çalışıyoruz
        while (currentNode != null) {
            if (parkingLot.getCapacity() > currentNode.element.getCapacity()) {
                // Geçici predecessor olarak kaydet
                predecessor = currentNode;
                currentNode = currentNode.rightChild; // Büyük değerler için sağa git
            } else if (parkingLot.getCapacity() < currentNode.element.getCapacity()) {
                currentNode = currentNode.leftChild; // Küçük değerler için sola git
            } else {
                break; // Düğümü bulduğumuzda döngüden çık
            }
        }

        // Eğer sol alt ağaç varsa, sol alt ağacın en sağ düğümünü bul
        if (currentNode != null && currentNode.leftChild != null) {
            predecessor = currentNode.leftChild;
            while (predecessor.rightChild != null) {
                predecessor = predecessor.rightChild; // Sol alt ağacın en sağ düğümüne git
            }
        }

        return predecessor != null ? predecessor.element : null; // İn-order öncülü döndür veya null
    }

    public ParkingLot findInOrderPredecessor(int capacity) {
        Node current = rootNode;
        Node predecessor = null;

        while (current != null) {
            if (capacity <= current.element.getCapacity()) {
                // Eğer mevcut düğüm kapasitesi verilen kapasiteye eşit veya büyükse sola git
                current = current.leftChild;
            } else {
                // Eğer mevcut düğüm kapasitesi verilen kapasiteden küçükse, bu bir adaydır
                predecessor = current;
                // Daha büyük bir kapasite için sağa git
                current = current.rightChild;
            }
        }

        return predecessor != null ? predecessor.element : null; // Bulunan predecessor'ı veya null döndür
    }
    public ParkingLot findInOrderSuccessor(ParkingLot parkingLot) {
        Node successor = null;
        Node currentNode = rootNode;

        // İlk olarak, parkingLot düğümünü ve in-order ardılı bulmaya çalışıyoruz
        while (currentNode != null) {
            if (parkingLot.getCapacity() < currentNode.element.getCapacity()) {
                // Geçici successor olarak kaydet
                successor = currentNode;
                currentNode = currentNode.leftChild; // Küçük değerler için sola git
            } else if (parkingLot.getCapacity() > currentNode.element.getCapacity()) {
                currentNode = currentNode.rightChild; // Büyük değerler için sağa git
            } else {
                break; // Düğümü bulduğumuzda döngüden çık
            }
        }

        // Eğer sağ alt ağaç varsa, sağ alt ağacın en sol düğümünü bul
        if (currentNode != null && currentNode.rightChild != null) {
            successor = currentNode.rightChild;
            while (successor.leftChild != null) {
                successor = successor.leftChild; // Sağ alt ağacın en sol düğümüne gidin
            }
        }

        return successor != null ? successor.element : null; // İn-order ardılı döndür veya null
    }

    public ParkingLot findInOrderSuccessor(int capacity) {
        Node current = rootNode;
        Node successor = null;

        while (current != null) {
            if (capacity >= current.element.getCapacity()) {
                // Eğer mevcut düğüm kapasitesi verilen kapasiteye eşit veya küçükse sağa git
                current = current.rightChild;
            } else {
                // Eğer mevcut düğüm kapasitesi verilen kapasiteden büyükse, bu bir adaydır
                successor = current;
                // Daha küçük bir kapasite aramak için sola git
                current = current.leftChild;
            }
        }

        return successor != null ? successor.element : null; // Bulunan successor'ı veya null döndür
    }
}


