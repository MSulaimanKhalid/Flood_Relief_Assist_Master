
class ResourceDistribution{

    static final int MAX_NODES = 50;
    static final int MAX_DEGREE = 8;
    static final int MAX_AFFECTEES = 200;
    static final int MAX_CAMPS = 20;
    static final int MAX_CRATES_PER_ITEM = 200;
    static final int INF = 1_000_000;

    static final String[] ITEM_NAMES = {"Food", "Water", "Blanket", "Medicine"};
    static final int ITEM_COUNT = ITEM_NAMES.length;

    static class Graph {
        int n;
        int[][] adj;
        int[] deg;

        Graph(int nodes) {
            n = nodes;
            adj = new int[nodes][MAX_DEGREE];
            deg = new int[nodes];
            for (int i = 0; i < nodes; i++) {
                deg[i] = 0;
            }
        }
        void addEdge(int u, int v) {
            if (deg[u]>=MAX_DEGREE||deg[v]>=MAX_DEGREE) {
                System.out.println("Warning: degree limit reached for node " + u + " or " + v);
                return;
            }
            adj[u][deg[u]++] = v;
            adj[v][deg[v]++] = u;
        }
    }
    static class IntQueue {
        int[] data;
        int head, tail, size, capacity;

        IntQueue(int cap) {
            capacity = cap;
            data = new int[cap];
            head = 0; tail = 0; size = 0;
        }

        boolean isEmpty() { return size == 0; }
        boolean isFull() { return size == capacity; }

        void enqueue(int x) {
            if (isFull()) {
                System.out.println("Queue overflow; enqueue ignored");
                return;
            }
            data[tail] = x;
            tail = (tail + 1) % capacity;
            size++;
        }

        int dequeue() {
            if (isEmpty()) {
                System.out.println("Queue underflow; returning -1");
                return -1;
            }
            int x = data[head];
            head = (head + 1) % capacity;
            size--;
            return x;
        }
    }


    static class Crate {
        String item;
        int qty;
        Crate(String item, int qty) { this.item = item; this.qty = qty; }
    }

    static class CrateStack {
        Crate[] data;
        int top;

        CrateStack(int cap) {
            data = new Crate[cap];
            top = -1;
        }

        boolean isEmpty() { return top < 0; }
        boolean isFull() { return top == data.length - 1; }

        void push(Crate c) {
            if (isFull()) {
                System.out.println("CrateStack overflow; push ignored");
                return;
            }
            data[++top] = c;
        }

        Crate pop() {
            if (isEmpty()) return null;
            Crate c = data[top];
            data[top] = null;
            top--;
            return c;
        }

        Crate peek() {
            if (isEmpty()) return null;
            return data[top];
        }
    }

    static class LogNode {
        String msg;
        LogNode next;
        LogNode(String m) { msg = m; next = null; }
    }

    static class LogList {
        LogNode head, tail;

        void add(String m) {
            LogNode n = new LogNode(m);
            if (head == null) { head = n; tail = n; }
            else { tail.next = n; tail = n; }
        }

        void print() {
            LogNode cur = head;
            while (cur != null) {
                System.out.println(cur.msg);
                cur = cur.next;
            }
        }
    }

    static class InvNode {
        String key;
        int qty;
        InvNode left, right;
        InvNode(String k, int q) { key = k; qty = q; left = null; right = null; }
    }

    static class InventoryBST {
        InvNode root;

        void insert(String key, int qty) {
            root = insertRec(root, key, qty);
        }

        InvNode insertRec(InvNode node, String key, int qty) {
            if (node == null) return new InvNode(key, qty);
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node.left = insertRec(node.left, key, qty);
            else if (cmp > 0) node.right = insertRec(node.right, key, qty);
            else node.qty += qty;
            return node;
        }

        InvNode search(String key) {
            InvNode cur = root;
            while (cur != null) {
                int cmp = key.compareTo(cur.key);
                if (cmp == 0) return cur;
                cur = (cmp < 0) ? cur.left : cur.right;
            }
            return null;
        }

        int getQty(String key) {
            InvNode n = search(key);
            return (n == null) ? 0 : n.qty;
        }

        void consume(String key, int amount) {
            InvNode n = search(key);
            if (n == null) return;
            n.qty = Math.max(0, n.qty - amount);
        }
    }

    static class Affectee {
        int id;
        int node;
        int priority;
        int[] needs;

        Affectee(int id, int node, int priority, int[] needs) {
            this.id = id;
            this.node = node;
            this.priority = priority;
            this.needs = needs;
        }
    }

    static class Camp {
        int id, node;

        Affectee[] heap = new Affectee[MAX_AFFECTEES];
        int heapSize = 0;
        CrateStack[] itemStacks = new CrateStack[ITEM_COUNT];

        Camp(int id, int node) {
            this.id = id;
            this.node = node;
            for (int i = 0; i < ITEM_COUNT; i++) {
                itemStacks[i] = new CrateStack(MAX_CRATES_PER_ITEM);
            }
        }
        void addCrate(String itemName, int qty) {
            int idx = itemIndex(itemName);
            if (idx == -1) {
                System.out.println("Unknown item: " + itemName);
                return;
            }
            itemStacks[idx].push(new Crate(itemName, qty));
        }
        int itemIndex(String itemName) {
            for (int i = 0; i < ITEM_COUNT; i++) {
                if (ITEM_NAMES[i].equals(itemName)) return i;
            }
            return -1;
        }

        void insert(Affectee a) {
            heap[heapSize] = a;
            int i = heapSize;
            heapSize++;

            while (i > 0) {
                int parent = (i - 1) / 2;
                if (heap[i].priority > heap[parent].priority) {
                    Affectee tmp = heap[i];
                    heap[i] = heap[parent];
                    heap[parent] = tmp;
                    i = parent;
                } else break;
            }
        }

        Affectee extractMax() {
            if (heapSize == 0) return null;
            Affectee max = heap[0];
            heapSize--;
            heap[0] = heap[heapSize];
            heap[heapSize] = null;
            int i = 0;
            while (true) {
                int l = i * 2 + 1;
                int r = i * 2 + 2;
                int largest = i;
                if (l < heapSize && heap[l].priority > heap[largest].priority) largest = l;
                if (r < heapSize && heap[r].priority > heap[largest].priority) largest = r;
                if (largest != i) {
                    Affectee tmp = heap[i];
                    heap[i] = heap[largest];
                    heap[largest] = tmp;
                    i = largest;
                } else break;
            }
            return max;
        }

        boolean hasAffectees() { return heapSize > 0; }
    }

    static void assignAffecteesToCamps(Graph g, Camp[] camps, int campCount,Affectee[] aff, int affCount) {
        int[] dist = new int[g.n];
        int[] nearestCampId = new int[g.n];
        for (int i = 0; i < g.n; i++) { dist[i] = INF; nearestCampId[i] = -1; }

        IntQueue q = new IntQueue(g.n * 2);
        for (int i = 0; i < campCount; i++) {
            int cn = camps[i].node;
            dist[cn] = 0;
            nearestCampId[cn] = camps[i].id;
            q.enqueue(cn);
        }

        while (!q.isEmpty()) {
            int u = q.dequeue();
            for (int k = 0; k < g.deg[u]; k++) {
                int v = g.adj[u][k];
                if (dist[v] == INF) {
                    dist[v] = dist[u] + 1;
                    nearestCampId[v] = nearestCampId[u];
                    q.enqueue(v);
                }
            }
        }
        for (int i = 0; i < affCount; i++) {
            Affectee a = aff[i];
            int cid = nearestCampId[a.node];
            Camp target = null;
            for (int j = 0; j < campCount; j++) {
                if (camps[j].id == cid) { target = camps[j]; break; }
            }
            if (target == null) {
                System.out.println("Affectee#" + a.id + "  not assigned to any camp.");
            } else {
                target.insert(a);
            }
        }
    }

    static void distribute(Camp[] camps, int campCount, InventoryBST globalInv, LogList logs) {
        for (int i = 0; i < campCount; i++) {
            Camp c = camps[i];
            logs.add(" Serving Camp " + c.id + " at node " + c.node + " ");

            while (c.hasAffectees()) {
                Affectee a = c.extractMax();
                logs.add("Affectee#" + a.id + " (priority=" + a.priority + ")");

                for (int itemIdx = 0; itemIdx < ITEM_COUNT; itemIdx++) {
                    String itemName = ITEM_NAMES[itemIdx];
                    int need = a.needs[itemIdx];
                    if (need <= 0) continue;

                    int allocated = 0;

                    while (need > 0 && !c.itemStacks[itemIdx].isEmpty()) {
                        Crate top = c.itemStacks[itemIdx].peek();
                        int take = Math.min(need, top.qty);
                        top.qty -= take;
                        need -= take;
                        allocated += take;
                        logs.add("  From Camp Stack: +" + take + " " + itemName + " (crate left=" + top.qty + ")");
                        if (top.qty == 0) c.itemStacks[itemIdx].pop();
                    }

                    if (need > 0) {
                        int avail = globalInv.getQty(itemName);
                        int take = Math.min(need, avail);
                        globalInv.consume(itemName, take);
                        need -= take;
                        allocated += take;
                        logs.add("  From Global Store: +" + take + " " + itemName + " (global left=" + globalInv.getQty(itemName) + ")");
                    }

                    a.needs[itemIdx] -= allocated;
                    logs.add("  -> " + itemName + " allocated " + allocated + " (remaining need=" + a.needs[itemIdx] + ")");
                }

                boolean allMet = true;
                for (int k = 0; k < ITEM_COUNT; k++) {
                    if (a.needs[k] > 0) { allMet = false; break; }
                }
                if (allMet) logs.add("Affectee#" + a.id + " fully served.");
                else logs.add("Affectee#" + a.id + " partially served (remaining needs kept for records).");
            }
        }
    }

    public static void main(String[] args) {

        Graph g = new Graph(12);
        g.addEdge(0,1); g.addEdge(1,2); g.addEdge(2,3); g.addEdge(3,4);
        g.addEdge(1,5); g.addEdge(5,6); g.addEdge(6,7); g.addEdge(4,8);
        g.addEdge(8,9); g.addEdge(7,10); g.addEdge(9,11);

        int campCount = 2;
        Camp[] camps = new Camp[campCount];
        camps[0] = new Camp(100, 3);
        camps[1] = new Camp(200, 9);

        camps[0].addCrate("Food", 20);
        camps[0].addCrate("Food", 15);
        camps[0].addCrate("Water", 30);
        camps[0].addCrate("Water", 25);
        camps[0].addCrate("Blanket", 10);
        camps[0].addCrate("Medicine", 8);

        camps[1].addCrate("Food", 25);
        camps[1].addCrate("Water", 35);
        camps[1].addCrate("Blanket", 12);
        camps[1].addCrate("Medicine", 5);


        int affCount = 5;
        Affectee[] aff = new Affectee[affCount];
        aff[0] = new Affectee(1, 0, 9, new int[]{2, 4, 0, 1});
        aff[1] = new Affectee(2, 1, 5, new int[]{1, 2, 1, 0});
        aff[2] = new Affectee(3, 7, 10, new int[]{3, 3, 2, 2});
        aff[3] = new Affectee(4, 11, 4, new int[]{0, 2, 1, 1});
        aff[4] = new Affectee(5, 6, 7, new int[]{2, 1, 0, 0});

        InventoryBST globalInv = new InventoryBST();
        globalInv.insert("Food", 100);
        globalInv.insert("Water", 120);
        globalInv.insert("Blanket", 50);
        globalInv.insert("Medicine", 40);
        assignAffecteesToCamps(g, camps, campCount, aff, affCount);

        LogList logs = new LogList();
        distribute(camps, campCount, globalInv, logs);

        System.out.println(" DISTRIBUTION LOGS ");
        logs.print();
        System.out.println(" REMAINING GLOBAL INVENTORY ");
        for (int i = 0; i < ITEM_COUNT; i++) {
            String item = ITEM_NAMES[i];
            System.out.println(item + ": " + globalInv.getQty(item));
        }
    }
}
