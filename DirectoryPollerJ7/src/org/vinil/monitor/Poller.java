package org.vinil.monitor;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

public class Poller {

	public static void main(String[] args) {

		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

			Map<WatchKey, Path> keyMap = new HashMap<>();
			Path myPath = Paths.get("//home//vinil//workspace//DirectoryPoller//MyFiles");
			keyMap.put(myPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY), myPath);

			WatchKey myWatchKey;

			do {
				myWatchKey = watchService.take();
				Path eventDir = keyMap.get(myWatchKey);

				for (WatchEvent<?> event : myWatchKey.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					Path eventPath = (Path) event.context();
					if (kind.toString().equalsIgnoreCase("ENTRY_CREATE")) {
						doSomething(myWatchKey, eventPath);
					}
					System.out.println(eventDir + "\t " + kind + "\t" + eventPath);
				}
			} while (myWatchKey.reset());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doSomething(WatchKey myWatchKey, Path eventPath) {
		System.out.println("The control goes out to do something else");
		try {
			Files.delete(eventPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// myWatchKey.reset();
	}

}
