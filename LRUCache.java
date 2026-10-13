import java.util.*;
class LRUCache {
    class Node {
        int key;
        int value;
        Node prev;
        Node next;
    }
    private Hashtable<Integer,Node> cache = new Hashtable<>();
    private int count;
    private int capacity;
    private Node head,tail;

    private void addNode(Node node){
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    private void removeNode(Node node){
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
    }
    private void moveToHead(Node node){
        removeNode(node);
        addNode(node);
    }
    private Node popTail(){
        Node temp = tail.prev;
        removeNode(temp);
        return temp;
    }
    public LRUCache(int capacity) {
        this.count = 0;
        this.capacity = capacity;
        head = new Node();
        head.prev = null;
        tail = new Node();
        tail.next = null;
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        Node node = cache.get(key);
        if(node == null){
            return -1;
        }
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        Node node = cache.get(key);
        if(node == null){
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            cache.put(key,newNode);
            addNode(newNode);
            ++count;

            if(count>capacity){
                Node tailNode = popTail();
                cache.remove(tailNode.key);
                --count;
            }
        }else{
            node.value = value;
            moveToHead(node);
        }

    }
     public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the capacity of the LRU Cache: ");
        int capacity = scanner.nextInt();
        LRUCache cache = new LRUCache(capacity);
        System.out.println("LRU Cache with capacity " + capacity + " created.");

        while (true) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Put (key, value)");
            System.out.println("2. Get (key)");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter key: ");
                    int keyToPut = scanner.nextInt();
                    System.out.print("Enter value: ");
                    int valueToPut = scanner.nextInt();
                    cache.put(keyToPut, valueToPut);
                    System.out.println("Added/Updated: (" + keyToPut + ", " + valueToPut + ")");
                    break;

                case 2:
                    System.out.print("Enter key: ");
                    int keyToGet = scanner.nextInt();
                    int result = cache.get(keyToGet);
                    if (result != -1) {
                        System.out.println("Value for key " + keyToGet + " is: " + result);
                    } else {
                        System.out.println("Key " + keyToGet + " not found in cache.");
                    }
                    break;

                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */