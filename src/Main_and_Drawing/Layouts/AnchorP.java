package Main_and_Drawing.Layouts;

public class AnchorP extends LayoutParameters {
	public final static int left = 1;
	public final static int middle = 2;
	public final static int right = 3;
	public final static int top = 4;
	public final static int bottom = 6;
	
	public int xLayoutAnchor;
	public int xSelfAnchor;
	public int xDistance;
	public int yLayoutAnchor;
	public int ySelfAnchor;
	public int yDistance;
	
	
	public AnchorP(int xLayoutAnchor, int xSelfAnchor, int xDistance,
						int yLayoutAnchor, int ySelfAnchor, int yDistance){
		this.xLayoutAnchor = xLayoutAnchor;
		this.xSelfAnchor = xSelfAnchor;
		this.xDistance = xDistance;
		this.yLayoutAnchor = yLayoutAnchor;
		this.ySelfAnchor = ySelfAnchor;
		this.yDistance = yDistance;
	}
	
	@Override
	public LayoutParameters copy() {
		return new AnchorP(xLayoutAnchor, xSelfAnchor, xDistance,
		yLayoutAnchor, ySelfAnchor, yDistance);
	}
	
	
	
	@Override
	public int getX() {
		int x = 0;
		switch(xLayoutAnchor){
			case left:
				x = parent.getX();
				break;
			case middle:
				x = parent.getX() + parent.getWidth()/2;
				break;
			case right:
				x = parent.getX() + parent.getWidth();
				break;
		}
		switch(xSelfAnchor){
			case left:
				x -= 0;
				break;
			case middle:
				x -= twod.getWidth()/2;
				break;
			case right:
				x -= twod.getWidth();
				break;
		}
		return x;
	}
	
	@Override
	public int getY() {
		int y = 0;
		switch(yLayoutAnchor){
			case top:
				y = parent.getY();
				break;
			case middle:
				y = parent.getY() + parent.getHeight()/2;
				break;
			case bottom:
				y = parent.getY() + parent.getHeight();
				break;
		}
		switch(ySelfAnchor){
			case top:
				y -= 0;
				break;
			case middle:
				y -= twod.getHeight()/2;
				break;
			case bottom:
				y -= twod.getHeight();
				break;
		}
		return y;
	}
	
	
}
