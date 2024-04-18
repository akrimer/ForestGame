import java.util.List;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;
/*

class AStarPathingStrategy implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();
        */
/*define closed list
          define open list
          while (true){
            Filtered list containing neighbors you can actually move to
            Check if any of the neighbors are beside the target
            set the g, h, f values
            add them to open list if not in open list
            add the selected node to close list
          return path*//*

         return path;
    }
}
*/

import java.util.*;

class AStarPathingStrategy implements PathingStrategy{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        LinkedList<Point> computedPath_List = new LinkedList<>();
        Map<Point, Node> closedMap = new HashMap<>();
        Map<Point, Node> openMap = new HashMap<>();
        Queue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(Node::getF));

        Node startNode = new Node(0, heuristic(start, end), 0 + heuristic(start, end), start, null);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.remove();

            if (withinReach.test(current.getPosition(), end)) {
                return computedPath(computedPath_List, current);
            }

            List<Point> neighbors = potentialNeighbors.apply(current.getPosition())
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start) && !p.equals(end))
                    .collect(Collectors.toList());

            for (Point neighbor : neighbors) {
                if (!closedMap.containsKey(neighbor)) {
                    int temp_g = current.getG() + 1;

                    if (openMap.containsKey(neighbor)) {
                        if (temp_g < openMap.get(neighbor).getG()) {
                            Node betterNode = new Node(temp_g, heuristic(neighbor, end), temp_g + heuristic(neighbor, end), neighbor, current);
                            openList.remove(openMap.get(neighbor));
                            openList.add(betterNode);
                            openMap.replace(neighbor, betterNode);
                        }
                    } else {
                        Node neighborNode = new Node(temp_g, heuristic(neighbor, end), temp_g + heuristic(neighbor, end), neighbor, current);
                        openList.add(neighborNode);
                        openMap.put(neighbor, neighborNode);
                    }
                }
            }

            closedMap.put(current.getPosition(), current);
        }

        System.out.println("No path found.");
        return computedPath_List;
    }

    public List<Point> computedPath(LinkedList<Point> compPath, Node winner) { // Changed to LinkedList
        compPath.addFirst(winner.getPosition()); // Changed to addFirst for LinkedList

        if (winner.getPrevNode() == null) {
            return compPath;
        }

        return computedPath(compPath, winner.getPrevNode());
    }



    public List<Point> computedPath(List<Point> compPath, Node winner) {
        compPath.add(winner.getPosition());

        if (winner.getPrevNode() == null) {
            Collections.reverse(compPath);
            return compPath;
        }

        return computedPath(compPath, winner.getPrevNode());
    }


    public void printOpenList(Queue<Node> openList) {

        openList.stream().forEach(n->System.out.println(n));
    }



    //calualte the heuristic
    public int heuristic(Point current, Point goal) {
        Point currentPoint = new Point(current.x, current.y); // Assuming Point has a constructor
        return currentPoint.distanceSquared(goal);
    }


    //A* data structure
    class Node {
        private int g; //distance from start
        private int h; //heursitc distance
        private int f; //total distance f=g+h
        private Node last_node; // prior node;
        private Point position;

        public Node (int g, int h, int f, Point position, Node prev_node){
            this.g = g; //no distance from the start
            this.h = h; // heuristic distance from the gaol
            this.f = f; //total distance f = g+h
            this.last_node = prev_node; //parent node;
            this.position = position;
            // this.currentPoint = currentPoint;
        }

        //used to map p -> node
        public boolean containsPoint(Point p ){

            if(this.position == p){
                return true;
            }
            else{

                return false;
            }
        }

        public int getH(){return h;}
        public int getF(){return f;}
        public void setG(int g){this.g = g;}
        public void setH(int h){this.h = h;}
        public int getG(){return g;}
        public void setPostion(Point p){position = p;}
        public Point getPosition(){return position;}
        public Node getPrevNode(){return last_node;}
        public String toString(){return "getX() = "+ this.position.x + " getY() = " + this.position.y; }

    }
}
