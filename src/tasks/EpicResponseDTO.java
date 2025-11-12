package tasks;

import java.util.List;

public class EpicResponseDTO {
        public int id;
        public String name;
        public String description;
        public Status status;
        public String type;
        public List<Integer> subtaskIds;

        public EpicResponseDTO(Epic epic) {
            this.id = epic.getId();
            this.name = epic.getName();
            this.description = epic.getDescription();
            this.status = epic.getStatus();
            this.type = epic.getType().toString();
            this.subtaskIds = epic.getSubtaskIds();
        }
}
