
import java.util.*;

class TopologicalSort{
    private static void dfs(int node,int vis[],Stack<Integer> st,java.util.ArrayList<java.util.ArrayList<Integer>> adj){
        vis[node]=1;
        for(Integer it:adj.get(node)){
            if(vis[it]==0){
                dfs(it,vis,st,adj);
            }
        }
        st.push(node);
    }
    static int[] topoSort(int V, ArrayList<ArrayList<Integer>> adj){
        Stack<Integer> st=new Stack<>();
        int vis[]=new int[V];
        for(int i=0;i<V;i++){
            if(vis[i]==0){
                dfs(i,vis,st,adj);
            }
        }
        int ans[] = new int[V];
        int i=0;
        while(!st.isEmpty()){
            ans[i++]=st.peek();
            st.pop();
        }
        return ans;
    }
}