import New_Belt_Package.Balancer;
import New_Belt_Package.BeltGrid;
import org.junit.Test;

import java.awt.*;


public class Tester {
	
	
	@Test
	public void testBalancer() {
		
		
		BeltGrid belt_grid = new BeltGrid();
		for(int i = 0; i  < 4; i++){
			Balancer balancer = new Balancer(belt_grid, 3, 3, i);
			int[] space = balancer.space_taken();
			int[][] diff = balancer.get_affected_around();
			System.out.println("space: " + new Point(space[0], space[1]));
			for(int around = 0; around < 4; around++)
			System.out.println("affected belts: " + new Point(diff[around][0], diff[around][1]));
			
		}
		
		
	}
	
	@Test
	public void blah(){
		
		try{
			int num = 1;
			int den = 0;
			int result = num/den;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_log(){
	
	}
	
	// Additional test methods can be added here
}