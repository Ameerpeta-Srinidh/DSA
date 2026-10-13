import java.util.HashMap;
import java.util.Map;
class LFUCache {
    class Node{
        int key;
        int value;
        int count=1;
        Node prev;
        Node next;
    }
    class DoublyLinkedList{
        private Node head,tail;
        private int size=0;

    
    DoublyLinkedList(){
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }
        void addNode(Node node){
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
            node.prev = head;
            size++;
        }
        void removeNode(Node node){
            Node prev = node.prev;
            Node next = node.next;
            prev.next = next;
            next.prev = prev;
            size--;
        }
        Node getTail(){
            if(size>0){
            Node tailNode = tail.prev;
            removeNode(tailNode);
            return tailNode;
            }
            return null;
        }
    }
    private final int capacity;
    private int size;
    private Map<Integer,Node> cache;
    private Map<Integer,DoublyLinkedList> freqMap;
    private int minFrequency;


    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.minFrequency = 0;
        cache = new HashMap<>();
        freqMap = new HashMap<>();
    }
    private void updateNode(Node node){
        int freq = node.count;
        DoublyLinkedList list = freqMap.get(freq);
        list.removeNode(node);

        if(freq==minFrequency && list.size==0){
            minFrequency++;
        }
        node.count++;
        DoublyLinkedList newList = freqMap.getOrDefault(node.count,new DoublyLinkedList());
        newList.addNode(node);
        freqMap.put(node.count,newList);
    }
    
    public int get(int key) {
        Node node = cache.get(key);
        if(node == null){
            return -1;
        }
        updateNode(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        if(capacity ==0) return;
        if(cache.containsKey(key)){
            Node node = cache.get(key);
            node.value = value;
            updateNode(node);
        }else{
            if(size==capacity){
                DoublyLinkedList minFreqList = freqMap.get(minFrequency);
                Node removalNode = minFreqList.getTail();
                cache.remove(removalNode.key);
                size--;
            }
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;
            cache.put(key,newNode);

            DoublyLinkedList newList = freqMap.getOrDefault(1, new DoublyLinkedList());
            newList.addNode(newNode);
            freqMap.put(1,newList);
            this.minFrequency = 1;
            size++;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- LFU Cache Dry Run ---");
        System.out.println("Capacity: 2\n");

        // ["LFUCache","put","put","get","put","get","get","put","get","get","get"]
        // [[2],[1,1],[2,2],[1],[3,3],[2],[3],[4,4],[1],[3],[4]]
        LFUCache cache = new LFUCache(2);

        System.out.println("put(1, 1)");
        cache.put(1, 1);
        // Cache state: {key:1, freq:1}

        System.out.println("put(2, 2)");
        cache.put(2, 2);
        // Cache state: {key:1, freq:1}, {key:2, freq:1}
        // Within freq 1 list, 2 is MRU, 1 is LRU. minFrequency=1.

        System.out.println("get(1) -> returns: " + cache.get(1)); // Expected: 1
        // Key 1 is accessed. Freq becomes 2.
        // Cache state: {key:2, freq:1}, {key:1, freq:2}. minFrequency=1.

        System.out.println("put(3, 3)"); // This will cause an eviction.
        cache.put(3, 3);
        // Eviction check: minFrequency is 1. The LRU item in the freq 1 list is key 2.
        // Key 2 is evicted.
        // Cache state: {key:1, freq:2}, {key:3, freq:1}. minFrequency=1.

        System.out.println("get(2) -> returns: " + cache.get(2)); // Expected: -1 (evicted)

        System.out.println("get(3) -> returns: " + cache.get(3)); // Expected: 3
        // Key 3 is accessed. Freq becomes 2.
        // Cache state: {key:1, freq:2}, {key:3, freq:2}. minFrequency=2.

        System.out.println("put(4, 4)"); // This will cause an eviction.
        cache.put(4, 4);
        // Eviction check: minFrequency is 2.
        // Both 1 and 3 are in the freq 2 list.
        // Key 1 was accessed less recently than key 3, so key 1 is the LRU.
        // Key 1 is evicted.
        // Cache state: {key:3, freq:2}, {key:4, freq:1}. minFrequency=1.

        System.out.println("get(1) -> returns: " + cache.get(1)); // Expected: -1 (evicted)
        
        System.out.println("get(3) -> returns: " + cache.get(3)); // Expected: 3
        // Key 3 is accessed. Freq becomes 3.
        // Cache state: {key:4, freq:1}, {key:3, freq:3}. minFrequency=1.

        System.out.println("get(4) -> returns: " + cache.get(4)); // Expected: 4
        // Key 4 is accessed. Freq becomes 2.
        // Cache state: {key:3, freq:3}, {key:4, freq:2}. minFrequency=2.
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */