package org.eclipse.cdt.utils.pty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.cdt.utils.spawner.ProcessFactory;
import org.eclipse.core.runtime.Platform;

public class PTY2 {
	private Process terminalEmulator;
	private String slaveName;

	public PTY2() throws IOException {
		/*
		 * launch the patched terminal emulator process, and get the pty's slave name from stdout of the
		 * terminal emulator process
		 */
		String command = null;
		InputStream in = null;
		// Bundle bundle = Platform.getBundle(CNativePlugin.PLUGIN_ID);

		try {
			command = PTY2Util.getTerminalEmulatorCommand();

			/*
			 * both mintty.exe and konsole support --opentty and --title option
			 */
			String[] cmdArray;
			if (Platform.getOS().equals(Platform.OS_WIN32)) {
				cmdArray = new String[] { command, "--hold=always", //$NON-NLS-1$
						"--title", "Terminal Emulator" }; //$NON-NLS-1$ //$NON-NLS-2$
			} else if (Platform.getOS().equals(Platform.OS_LINUX)) {
				cmdArray = new String[] { command, "--nofork", "--hold", //$NON-NLS-1$ //$NON-NLS-2$
						"--title", "Terminal Emulator" }; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				cmdArray = new String[] { command, "--nofork", "--hold", //$NON-NLS-1$ //$NON-NLS-2$
						"--title", "Terminal Emulator" }; //$NON-NLS-1$ //$NON-NLS-2$
			}
			terminalEmulator = ProcessFactory.getFactory().exec(cmdArray,
					new String[] { "OPENPTY=" }); //$NON-NLS-1$

			in = terminalEmulator.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);

			String line = br.readLine();
			if (line != null) {
				slaveName = line;
			}

		} catch (IOException e) {

		} finally {
			// if (in != null)
			// in.close();
		}
		if (slaveName == null) {
			throw new IOException("can not start terminal emulator and get pty's slave name"); //$NON-NLS-1$
		}
	}

	public Process getTerminalEmulator() {
		return terminalEmulator;
	}

	public String getSlaveName() {
		return slaveName;
	}
}
