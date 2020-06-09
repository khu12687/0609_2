package excel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class AnimalModel extends AbstractTableModel{

	List<Animal> list = new ArrayList<Animal>();
	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Animal animal = list.get(row);
		String data = null;
		if(col ==0) {
			data=Integer.toString(animal.getAnimal_id());
		}else if(col ==1){
			data = animal.getName();
		}else if(col ==2){
			data = animal.getPhone();
		}else if(col ==3){
			data = animal.getEmail();
		}else if(col ==4){
			data = Integer.toString(animal.getAge());
		}
		return data;
	}

}
