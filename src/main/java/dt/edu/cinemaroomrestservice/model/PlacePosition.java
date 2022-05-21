package dt.edu.cinemaroomrestservice.model;

/**
 * Позиция места кинотеатра. Состоит из 2х полей:
 * row - ряд,
 * column - место в ряду
 */
public class PlacePosition {
    int row;
    int column;

    @Override
    public String toString() {
        return "PlacePosition{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlacePosition that = (PlacePosition) o;

        if (row != that.row) return false;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    public PlacePosition() {
    }

    public PlacePosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
