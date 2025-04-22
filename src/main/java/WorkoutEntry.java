import javafx.beans.property.*;

public class WorkoutEntry {
    private final StringProperty workoutDate;
    private final IntegerProperty duration;
    private final StringProperty notes;
    private final StringProperty planName;

    public WorkoutEntry(String date, int duration, String notes, String planName) {
        this.workoutDate = new SimpleStringProperty(date);
        this.duration = new SimpleIntegerProperty(duration);
        this.notes = new SimpleStringProperty(notes);
        this.planName = new SimpleStringProperty(planName == null ? "N/A" : planName);
    }

    public StringProperty workoutDateProperty() { return workoutDate; }
    public IntegerProperty durationProperty() { return duration; }
    public StringProperty notesProperty() { return notes; }
    public StringProperty planNameProperty() { return planName; }

    public String getWorkoutDate() {
        return workoutDate.get();
    }

    public int getDuration() {
        return duration.get();
    }

    public String getNotes() {
        return notes.get();
    }
}
