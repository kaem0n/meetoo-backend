package kaem0n.meetoo.entities;

import java.time.LocalDate;
import java.util.UUID;

public class Group {
    private UUID id;
    private String name;
    private String description;
    private LocalDate creation;
    private Board board;
    private User founder;
}
