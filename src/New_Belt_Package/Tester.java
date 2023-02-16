package New_Belt_Package;

import org.junit.Test;

import java.awt.*;

public class Tester {
	
	
	@Test
	public void testBalancer() {
		
		
		Belt[][] belt_grid = new Belt[10][10];
		Balancer balancer = new Balancer(belt_grid, 3, 3, 0);
		int[] space = Balancer.get_extra_space_taken(0);
		System.out.println(new Point(space[0], space[1]));
	}
	
	// Additional test methods can be added here
}