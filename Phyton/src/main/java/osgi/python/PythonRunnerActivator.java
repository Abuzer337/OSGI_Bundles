package src.main.java.osgi.python;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonRunnerActivator implements BundleActivator {

    private Process pythonProcess;

    @Override
    public void start(BundleContext context) {
        System.out.println("[OSGi] Запуск Python-скрипта...");

        try {
            String pythonPath = "C:\\Users\\Ефим\\AppData\\Local\\Programs\\Python\\Python310\\python.exe";
            String scriptPath = "python_scripts/PhytonSnake.py"; // Указываем относительный путь

            ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptPath);
            pb.redirectErrorStream(true);
            pythonProcess = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Python]: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(BundleContext context) {
        System.out.println("[OSGi] Остановка Python-скрипта...");
        if (pythonProcess != null) {
            pythonProcess.destroy();
        }
    }
}
