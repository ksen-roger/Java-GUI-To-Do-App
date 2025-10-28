import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ToDoListApp extends JFrame {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInputField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearAllButton;
    private JLabel statusLabel;
    private ArrayList<Boolean> taskCompletionStatus;

    public ToDoListApp() {
        // Initialize components
        taskListModel = new DefaultListModel<>();
        taskCompletionStatus = new ArrayList<>();

        // Set up the frame
        setTitle("My To-Do List");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Title Label
        JLabel titleLabel = new JLabel("To-Do List Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 100));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for task list
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBackground(new Color(240, 240, 245));

        taskList = new JList<>(taskListModel);
        taskList.setFont(new Font("Arial", Font.PLAIN, 14));
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFixedCellHeight(30);

        // Add mouse listener for marking tasks as complete
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = taskList.getSelectedIndex();
                    if (index != -1) {
                        toggleTaskCompletion(index);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 210), 2));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Status label
        statusLabel = new JLabel("Total tasks: 0");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for input and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(240, 240, 245));

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(new Color(240, 240, 245));

        JLabel inputLabel = new JLabel("New Task:");
        inputLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(inputLabel, BorderLayout.WEST);

        taskInputField = new JTextField();
        taskInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        taskInputField.addActionListener(e -> addTask());
        inputPanel.add(taskInputField, BorderLayout.CENTER);

        bottomPanel.add(inputPanel, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));

        addButton = new JButton("Add Task");
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.setBackground(new Color(100, 200, 100));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addTask());

        deleteButton = new JButton("Delete Selected");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(220, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteTask());

        clearAllButton = new JButton("Clear All");
        clearAllButton.setFont(new Font("Arial", Font.BOLD, 14));
        clearAllButton.setBackground(new Color(150, 150, 150));
        clearAllButton.setForeground(Color.WHITE);
        clearAllButton.setFocusPainted(false);
        clearAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearAllButton.addActionListener(e -> clearAllTasks());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearAllButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // Instructions label
        JLabel instructionLabel = new JLabel("Tip: Double-click a task to mark as complete/incomplete");
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        instructionLabel.setForeground(new Color(100, 100, 150));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(instructionLabel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Focus on input field when application starts
        taskInputField.requestFocusInWindow();
    }

    private void addTask() {
        String task = taskInputField.getText().trim();
        if (!task.isEmpty()) {
            taskListModel.addElement("[ ] " + task);
            taskCompletionStatus.add(false);
            taskInputField.setText("");
            taskInputField.requestFocusInWindow();
            updateStatus();
            showMessage("Task added successfully!");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please enter a task!",
                    "Empty Task",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            String taskToDelete = taskListModel.getElementAt(selectedIndex);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete task: " + taskToDelete + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                taskListModel.remove(selectedIndex);
                taskCompletionStatus.remove(selectedIndex);
                updateStatus();
                showMessage("Task deleted successfully!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to delete!",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearAllTasks() {
        if (taskListModel.getSize() > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete all tasks?",
                    "Confirm Clear All",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                taskListModel.clear();
                taskCompletionStatus.clear();
                updateStatus();
                showMessage("All tasks cleared!");
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No tasks to clear!",
                    "Empty List",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void toggleTaskCompletion(int index) {
        boolean isCompleted = taskCompletionStatus.get(index);
        String currentTask = taskListModel.getElementAt(index);

        // Remove the checkbox prefix
        String taskText = currentTask.substring(4);

        if (isCompleted) {
            // Mark as incomplete
            taskListModel.set(index, "[ ] " + taskText);
            taskCompletionStatus.set(index, false);
        } else {
            // Mark as complete
            taskListModel.set(index, "[✓] " + taskText);
            taskCompletionStatus.set(index, true);
        }

        updateStatus();
    }

    private void updateStatus() {
        int total = taskListModel.getSize();
        int completed = 0;
        for (boolean status : taskCompletionStatus) {
            if (status) completed++;
        }
        statusLabel.setText("Total tasks: " + total + " | Completed: " + completed + " | Pending: " + (total - completed));
    }

    private void showMessage(String message) {
        Timer timer = new Timer(2000, e -> setTitle("My To-Do List"));
        timer.setRepeats(false);
        setTitle(message);
        timer.start();
    }

    public static void main(String[] args) {
        // Use system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            ToDoListApp app = new ToDoListApp();
            app.setVisible(true);
        });
    }
}