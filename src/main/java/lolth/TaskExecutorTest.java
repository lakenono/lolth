package lolth;

import java.util.Map;
import java.util.Set;

import lakenono.db.annotation.DBTable;
import lakenono.utils.AnnocationClassScaner;

public class TaskExecutorTest {

	public static void main(String[] args) {
		Map<Class<?>, Set<Class<?>>> map = AnnocationClassScaner.scan("lolth", DBTable.class);

		Set<Class<?>> set = map.get(DBTable.class);
		for (Class<?> clazz : set) {
			System.out.println(clazz.getName());
		}
	}
}
