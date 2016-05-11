package org.vinil.monitor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class ApacheFileMonitor {

	public static final String MONTIOR_FOLDER = "//home//vinil//workspace//DirectoryPollerApache//MyFiles";

	public static void main(String args[]) {
		final long pollingInterval = 20 * 1000;
		File folder = new File(MONTIOR_FOLDER);

		if (!folder.exists()) {

			System.out.println("Directory not found");
		}

		FileAlterationObserver observer = new FileAlterationObserver(folder);
		FileAlterationMonitor monitor = new FileAlterationMonitor(pollingInterval);

		FileAlterationListener listener = new FileAlterationListenerAdaptor() {
			public void onFileCreate(File file) {
				try {
					// "file" is the reference to the newly created file
					System.out.println("File created: " + file.getCanonicalPath());
					dosomething(file);
				} catch (IOException e) {
					e.printStackTrace(System.err);
				}
			}

			private void dosomething(File file) {
				System.out.println("Deleting the file: "+file.getAbsolutePath().toString());
				file.delete();
			}

			public void onFileDelete(File file) {
				try {
					// "file" is the reference to the removed file
					System.out.println("File removed: " + file.getCanonicalPath());
					// "file" does not exists anymore in the location
					System.out.println("File still exists in location: " + file.exists());
				} catch (IOException e) {
					e.printStackTrace(System.err);
				}

			}
		};
		observer.addListener(listener);
		monitor.addObserver(observer);
		try {
			monitor.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some issue in monitoring");
		}
	}
}
