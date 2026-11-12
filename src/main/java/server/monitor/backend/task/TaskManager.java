package server.monitor.backend.task;


import org.springframework.stereotype.Component;
import server.monitor.backend.client.PlayerClient;
import server.monitor.backend.client.TaskClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskManager {
    private final Map<UUID,Task> tasks = new ConcurrentHashMap<>();
    public void addTask(TaskClient.TaskRecieved taskRecieved) {
        UUID id = UUID.randomUUID();
        tasks.put(id,new Task(Task.Type.valueOf(taskRecieved.type()),id,taskRecieved.player_id(),taskRecieved.arguments()));
    }
    public Map<UUID,Task> getTasks() {
        return Map.copyOf(tasks);
    }
    public void clear(UUID id) {
        tasks.remove(id);
    }
}
