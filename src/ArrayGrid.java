
public class ArrayGrid<T> implements Grid<T> {

	public T[][] arry;
	public T[][] newArry;
	public int x_axis;
	public int y_axis;


	public ArrayGrid(int width, int height) throws IllegalArgumentException {

		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Failed,width or height equal or less than 0");
		}
		arry = (T[][]) new Object[width][height];
		x_axis = width;
		y_axis = height;

	}


	@Override
	public void add(int x, int y, T element) throws IllegalArgumentException {
		if (x < 0 || y < 0 || x >= x_axis || y >= y_axis) {
			throw new IllegalArgumentException("Failed,add x or y equal or less than 0, or over axis");
		} else {
			arry[x][y] = element;
		}

	}

	@Override
	public T get(int x, int y) throws IndexOutOfBoundsException {

		System.out.println(String.format("x:%d y:%d element:%s", x, y, arry[x][y]));
		if (x >= arry.length || y >= arry[0].length) {
			throw new IllegalArgumentException("Failed, get x or y equal or less than 0, or over axis");
		} else if (arry[x][y] == null) {
			//System.out.println("null");
			return null;
		} else {
			return arry[x][y];
		}
	}

	@Override
	public boolean remove(int x, int y) throws IndexOutOfBoundsException {
		if (x < 0 || y < 0 || x >= x_axis || y >= y_axis) {
			return false;
		} else if (arry[x][y] == null) {
			return false;
		} else {
			arry[x][y] = null;
			return true;
		}

	}

	@Override
	public void clear() {
		int x = 0, y = 0;
		while (x < x_axis) {
			while (y < y_axis) {
				arry[x][y] = null;
				y++;
			}
			x++;
		}
	}

	@Override
	public void resize(int newWidth, int newHeight) throws IllegalArgumentException {
		if (-newWidth > x_axis || -newHeight > y_axis){
			throw new IllegalArgumentException("axis change decreased too much, the axis won't exist");
		}
		newArry = (T[][]) new Object[newWidth][newHeight];
		for (int x = 0; x < x_axis; x++){
			for (int y = 0; y < y_axis; y++){
				if (arry[x][y] != null){
					newArry[x][y] = arry[x][y];
				}
			}
		}
		arry = newArry;
	}
}


