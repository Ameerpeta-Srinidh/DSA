import java.util.*;

class MedianFinder{
    PriorityQueue<Integer> pq1 = new PriorityQueue<>(Collections.reverseOrder());
    PriorityQueue<Integer> pq2 = new PriorityQueue<>();
    MedianFinder(){
      
    }
    void addNum(int num){
       pq1.add(num);
       pq2.add(pq1.poll());

       if(pq2.size()>pq1.size()){
        pq1.add(pq2.poll());
       }
    }

    double findMedian(){
       double median;
       if(pq1.size() != pq2.size()){
        median = pq1.peek();
       }else{
        median = (pq1.peek()+pq2.peek())/2.0;
       }
       return median;
    }
}