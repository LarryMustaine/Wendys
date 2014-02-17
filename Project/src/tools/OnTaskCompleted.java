package tools;

public interface OnTaskCompleted
{
    public void onTaskCompleted(String logFilePath);
    public void onTaskError();
}