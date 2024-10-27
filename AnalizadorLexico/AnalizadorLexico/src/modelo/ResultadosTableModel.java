package modelo;

import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ResultadosTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Línea", "Columna", "Tipo", "Valor"};
    private final List<Token> tokens;

    public ResultadosTableModel(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public int getRowCount() {
        return tokens.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Token token = tokens.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> token.getLinea();
            case 1 -> token.getColumna();
            case 2 -> token.getTipoDescriptivo();
            case 3 -> token.getValor();
            default -> null;
        };
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0, 1 -> Integer.class;
            default -> String.class;
        };
    }
}