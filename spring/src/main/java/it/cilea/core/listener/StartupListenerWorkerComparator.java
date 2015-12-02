package it.cilea.core.listener;

import java.io.Serializable;
import java.util.Comparator;

public class StartupListenerWorkerComparator implements Comparator<StartupListenerWorker>, Serializable {

	public int compare(StartupListenerWorker element1, StartupListenerWorker element2) {
		Integer priority1 = element1.getPriority();
		Integer priority2 = element2.getPriority();
		if (priority1 != null && priority2 != null && !priority1.equals(priority2))
			return priority1.compareTo(priority2);
		if (priority1 == null && priority2 != null)
			return 1;
		if (priority1 != null && priority2 == null)
			return -1;
		return element1.getClass().toString().compareTo(element2.getClass().toString());
	}
}
