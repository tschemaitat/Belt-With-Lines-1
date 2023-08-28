package Data_Structure_Benchmark;

public class Manager_Item_ArrayQueue {
	public static void main(String[] args){
		new Manager_Item_ArrayQueue();
	}
	
	public static void basic_loop(){
		Item_Array array1 = new Item_Array(10);
		Item_Array array2 = new Item_Array(10);
		array1.set_output(array2, 9);
		array2.set_output(array1, 9);
		
		for(int i = 0; i < 4; i++)
			array1.insert(i * 2, 5);
		for(int i = 0; i < 4; i++)
			array2.insert(i * 2, 5);
		array1.print_items();
		array2.print_items();
		System.out.println("starting iterations***********");
		for(int i = 0; i < 100; i++){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			System.out.println("@ LOOPING @");
			array1.set_not_iterated();
			array2.set_not_iterated();
			//System.out.println("iterating 1: ");
			array1.iterate();
			//System.out.println("iterating 2: ");
			array2.iterate();
			//System.out.println("array 1: ");
			array1.print_items();
			//array1.print_debug_info();
			//System.out.println("array 2: ");
			array2.print_items();
			//array2.print_debug_info();
		}
	}
	public Manager_Item_ArrayQueue(){
	
	}
}
