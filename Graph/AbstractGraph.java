package Graph;

/**
 * AbstractGraph
 * 图的抽象类
 */
public abstract class AbstractGraph<T> {
    protected static final int MAX_WEIGHT = 0x0000ffff;//定义了最大的值
    protected SeqList<T> vertexlist;//一个顺序表，用来存顶点
    public AbstractGraph(int length){//构造器1，传入大小，构造相应大小的顺序表
        this.vertexlist = new SeqList<T>(length);
    }
    public AbstractGraph(){//构造器2，创建默认大小10的顺序表
        this(10);
    }
    public int vertexCount(){//定点数量
        return this.vertexlist.size();
    }
    public String toString(){//返回当前点集情况
        return "顶点集合："+this.vertexlist.toString()+"\n";
    }
    public T getVertex(int i){//根据下表返回当前某一个节点的值
        return this.vertexlist.get(i);
    }
    public void setVertex(int i,T x){//重设某一个节点
        this.vertexlist.set(i,x);
    }
    public abstract int insertVertex(T x);//插入节点
    public abstract void removeVertex(int i);//移除节点以及关联的边
    public abstract int weight(int i,int j);//节点的权
    protected abstract int next(int i,int j);//返回i在j后的临接节点的序号

    //深度优先遍历，变量i指的是顶点集合的下标（从哪个节点进入）
    public void DFSTraverse(int i)                          
    {
        boolean[] visited=new boolean[this.vertexCount()];  //访问数组，访问过后标记。大小和点集合一样
        int j=i;
        do
        {   if (!visited[j])//一开始，visited数组都默认为false。当节点还没有访问过的时候进入                                
            {
                System.out.print("{ ");
                this.depthfs(j, visited);//调用递归函数，打印路径                   
                System.out.print("} ");
            }
            j = (j+1) % this.vertexCount();//在其他连通分量中找未被访问的节点                 
        } while (j!=i);//j = i时，
        System.out.println();
    }
     
    private void depthfs(int i, boolean[] visited)
    {
        System.out.print(this.getVertex(i)+" ");            
        visited[i] = true;                                  
        for (int j=this.next(i,-1); j!=-1; j=this.next(i,j)) 
            if(!visited[j])                                 
                depthfs(j, visited);                        
    }

     
    public void BFSTraverse(int i)                          
    {
        boolean[] visited = new boolean[this.vertexCount()];  
        int j=i;
        do
        {   if (!visited[j])                                
            {
                System.out.print("{ ");
                breadthfs(j, visited);                      
                System.out.print("} ");
            }
            j = (j+1) % this.vertexCount();                 
        } while (j!=i);
        System.out.println();
    }
        
     
    private void breadthfs(int i, boolean[] visited)
    {
        System.out.print(this.getVertex(i)+" ");            
        visited[i] = true;                                  
 
        LinkedQueue<Integer> que = new LinkedQueue<Integer>();    
        que.add(i);                                         
        while (!que.isEmpty())                              
        {
            i = que.poll();                                 
            for (int j=next(i,-1); j!=-1; j=next(i,j))      
                if (!visited[j])                            
                {
                    System.out.print(this.getVertex(j)+" "); 
                    visited[j] = true;
                    que.add(j);                             
 
                }
        }
    }

     
     
    public void minSpanTree()
    {
        Triple[] mst = new Triple[vertexCount()-1];         
        for (int i=0; i<mst.length; i++)                    
            mst[i]=new Triple(0,i+1,this.weight(0,i+1));    

        for (int i=0; i<mst.length; i++)                    
        {
            System.out.print("mst边集合：");
            for(int j=0; j<mst.length; j++)
                System.out.print(mst[j].toString()+",");            
            System.out.println();            
            
 
            int min=i; 
            for (int j=i+1; j<mst.length; j++)              
                if (mst[j].value < mst[min].value) 
                {
 
                    min = j;                                
                }
            
             
            Triple edge = mst[min];
            if (min!=i)
            {
                mst[min] = mst[i];
                mst[i] = edge;
            }
        
             
            int tv = edge.column;                           
            for (int j=i+1; j<mst.length; j++)
            {
                int v = mst[j].column;                      
                int weight = this.weight(tv,v);
                if (weight<mst[j].value)                    
                    mst[j] = new Triple(tv,v,weight);
            }
        }
        
        System.out.print("\n最小生成树的边集合：");
        int mincost=0;
        for (int i=0; i<mst.length; i++)                    
        {
            System.out.print(mst[i]+" ");
            mincost += mst[i].value;
        }
        System.out.println("，最小代价为"+mincost);
    }

     
     
    public void shortestPath(int i)               
    {
        int n = this.vertexCount();               
        boolean[] vset = new boolean[n];          
        vset[i] = true;                           
        int[] dist = new int[n];                  
        int[] path = new int[n];                  
        for (int j=0; j<n; j++)                   
        {
            dist[j] = this.weight(i,j);
            path[j] = (j!=i && dist[j]<MAX_WEIGHT) ? i : -1;
        }
     
        for (int j=(i+1)%n; j!=i; j=(j+1)%n)      
        {
            int mindist=MAX_WEIGHT, min=0;        
            for (int k=0; k<n; k++)
                if (!vset[k] && dist[k]<mindist)
                {
                    mindist = dist[k];            
                    min = k;                      
                }
            if (mindist==MAX_WEIGHT)              
                break;
            vset[min] = true;                     
            for (int k=0; k<n; k++)               
                if (!vset[k] && this.weight(min,k)<MAX_WEIGHT && dist[min]+this.weight(min,k)<dist[k])
                {
                    dist[k] = dist[min] + this.weight(min,k); 
                    path[k] = min;                    
                }    
 
        }

        System.out.print(this.getVertex(i)+"的单源最短路径：");
        for (int j=0; j<n; j++)                             
            if (j!=i)
            {
                SinglyList<T> pathlink = new SinglyList<T>(); 
                pathlink.insert(0, this.getVertex(j));      
                for (int k=path[j]; k!=i && k!=j && k!=-1; k=path[k])
                    pathlink.insert(0, this.getVertex(k));  
                pathlink.insert(0, this.getVertex(i));      
                System.out.print(pathlink.toString()+"长度"+(dist[j]==MAX_WEIGHT ? "∞" : dist[j])+"，");
            }
        System.out.println();
    }
    // private static String toString(int[] value)         
    // {
    //     if (value!=null && value.length>0)
    //     {
    //         String str="{";
    //         int i=0;
    //         for(i=0; i<value.length-1; i++)
    //             str += (value[i]==MAX_WEIGHT ? "∞" : value[i])+",";
    //         return str+(value[i]==MAX_WEIGHT ? "∞" : value[i])+"}";
    //     }
    //     return null;        
    // }
     

   
     
    public void shortestPath()                   
    {
        int n=this.vertexCount();                           
        Matrix path=new Matrix(n), dist=new Matrix(n);      
        for (int i=0; i<n; i++)                             
            for (int j=0; j<n; j++)
            {   int w=this.weight(i,j);
                dist.set(i,j,w);                            
                path.set(i,j, (i!=j && w<MAX_WEIGHT ? i : -1));
            }
        System.out.println("dist"+dist.toString()+"path"+path.toString()+"路径矩阵：");
        printPathAll(path);

        for (int k=0; k<n; k++)                             
        {
            System.out.println("\n以"+this.getVertex(k)+"作为中间顶点，替换路径如下：");
            for (int i=0; i<n; i++)                         
                if (i!=k)
                    for (int j=0; j<n; j++)
                        if (j!=k && j!=i)
                        {
                        System.out.print(toPath(path,i,j)+"路径长度"+dist.get(i,j)+"，替换为"+
                            toPath(path,i,k)+","+toPath(path,k,j)+"路径长度"+(dist.get(i,k)+dist.get(k,j))+"？");
                        if (j!=k && j!=i && dist.get(i,j) > dist.get(i,k)+dist.get(k,j)) 
                        {
                            dist.set(i, j, dist.get(i,k)+dist.get(k,j));
                            path.set(i, j, path.get(k,j));
                            System.out.println("是，d"+i+j+"="+dist.get(i,j)+"，p"+i+j+"="+path.get(i,j));
                        }
                        else
                            System.out.println("否");
                        }
            System.out.println("dist"+dist.toString()+"path"+path.toString()+"路径矩阵：");
            printPathAll(path);
        }
    
        System.out.println("\n每对顶点间的最短路径如下：");
        for (int i=0; i<n; i++)
        {
            for (int j=0; j<n; j++)
                if (i!=j)
                    System.out.print(toPath(path,i,j)+"长度"+(dist.get(i,j)==MAX_WEIGHT ? "∞" : dist.get(i,j))+"，");
            System.out.println();
        }
    }
 
    
    private String toPath(Matrix path, int i, int j)            
    {
        SinglyList<T> pathlink = new SinglyList<T>(); 
        pathlink.insert(0, this.getVertex(j));      
        for (int k=path.get(i,j); k!=i && k!=j && k!=-1;  k=path.get(i,k))
            pathlink.insert(0, this.getVertex(k));  
        pathlink.insert(0, this.getVertex(i));      
        return pathlink.toString();
    }
    private void printPathAll(Matrix path)                  
    {
        for (int i=0; i<path.getRows(); i++)
        {
            for (int j=0; j<path.getRows(); j++)                
                System.out.print(toPath(path,i,j)+" ");
            System.out.println();
        }
    }

}