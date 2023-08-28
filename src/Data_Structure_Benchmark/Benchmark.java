package Data_Structure_Benchmark;

import java.util.Arrays;

public class Benchmark {
	static int num_lists = 100000;
	static int num_objects = 100;
	static int num_iterate = 1000;
	public static void main(String[] args){
		sum_of_log();
		
		
	}
	
	public static void benchmark(){
		System.out.println("num objects(millions): " + (num_lists*num_objects)/1000000);
		long start = System.currentTimeMillis();
		new Benchmark().test();
		//new Benchmark().test_c();
		long end = System.currentTimeMillis();
		long time = end-start;
		System.out.println("time in ms: " + time);
		
	}
	
	public static void sum_of_log(){
		int chunk = 256;
		int[] tiles = new int[32];
		for(int i = 0; i < tiles.length; i++){
			tiles[i] = (int)(Math.random() * 256);
		}
		double average = 0;
		double log_average = 0;
		Arrays.sort(tiles);
		for(int i = 0; i < tiles.length; i++){
			System.out.println(tiles[i]);
		}
		System.out.println("distance: \n");
		for(int i = 1; i < tiles.length; i++){
			int distance = tiles[i] - tiles[i - 1];
			System.out.println(distance);
			double log = Math.log((distance + 1) / Math.log(2));
			int bits = 0;
			
			if(distance <= 15){
				bits += 4;
			}else{
				bits += 8;
			}
			System.out.println("distance: " + distance + ", bits: " + bits);
			log_average += bits;
			average += distance;
		}
		System.out.println();
		
		
		System.out.println("average: " + average/tiles.length);
		System.out.println("log average: " + log_average/tiles.length);
	}
	
	public void test(){
		
		
		Item_ArrayQueue[] lists = new Item_ArrayQueue[num_lists];
		for(int i = 0; i < lists.length; i++){
			lists[i] = new Item_ArrayQueue(num_objects);
			for(int j = 0; j < num_objects; j++){
				lists[i].iterate();
				
			}
		}
		
		for(int i = 0; i < num_iterate; i++){
//			if(num_iterate/100 == i){
//				System.out.println("1%: " + (System.currentTimeMillis() - start));
//			}
			
			for(int j = 0; j < lists.length; j++){
				//Belt_Data_Structure list = lists[j];
				lists[j].iterate();
			}
		}
		
	}
	
	
	
//	private class Linked_List implements Belt_Data_Structure{
//		Node front = null;
//		Node back = null;
//		public Linked_List(){
//
//		}
//
//		public void insert(int index_from_front, Node node){
//
//		}
//
//		public Object pop(){
//			if(front == null){
//				return null;
//			}
//			Node result = front;
//			Node new_front = front.back;
//			new_front.front = null;
//			return result.payload;
//		}
//
//		public void insert_back(Object payload){
//			Node old_back = back;
//			back = new Node(payload);
//			old_back.back = back;
//			back.front = old_back;
//		}
//
//
//
//		private class Node{
//			public Node front;
//			public Node back;
//			public Object payload;
//			public Node(Object payload){
//				this.payload = payload;
//			}
//		}
//
//
//	}
	
	
}
