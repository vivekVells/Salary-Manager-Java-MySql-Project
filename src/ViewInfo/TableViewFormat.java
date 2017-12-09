package ViewInfo;

import java.util.*;

class Charr {
    
    protected static final char S = ' ';
    
    protected static final char NL = '\n';
    
    protected static final char P = '+';
    
    protected static final char D = '-';
    
    protected static final char VL = '|';

    private final int x;

    private final int y;

    private final char c;

    protected Charr(int x, int y, char c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    protected char getC() {
        return c;
    }

}
final class Table {
    
    private Board board;

    private final int tableWidth;

    private List<String> headersList;

    private List<List<String>> rowsList;

    private List<Integer> colWidthsList;

    private List<Integer> colAlignsList;

    private int headerHeight;

    private int rowHeight;

    private int gridMode;

    private Block initialTableBlock;

    public final static int GRID_NON = 13;

    public final static int GRID_FULL = 14;

    public final static int GRID_COLUMN = 15;

    public Table(Board board, int tableWidth, List<String> headersList, List<List<String>> rowsList) {
        this.board = board;
        if (tableWidth <= 0) {
            throw new RuntimeException("Board width must be large than zero. " + tableWidth + " given.");
        } else {
            this.tableWidth = tableWidth;
        }
        if (headersList.size() <= 0) {
            throw new RuntimeException("Header size must be large than zero. " + headersList.size() + " found.");
        } else {
            this.headersList = headersList;
        }
        for (int i = 0; i < rowsList.size(); i++) {
            List<String> row = rowsList.get(i);
            if (row.size() != headersList.size()) {
                throw new RuntimeException("Size(" + row.size() + ") of the row(" + i + ") and header size(" + headersList.size() + ") are not equal");
            }
        }
        this.rowsList = rowsList;
        this.colWidthsList = new ArrayList<>();
        int avgWidthOfCol = (tableWidth - (gridMode == GRID_NON ? 0 : headersList.size() + 1)) / headersList.size();
        int availableForExtend = (tableWidth - (gridMode == GRID_NON ? 0 : headersList.size() + 1)) % headersList.size();
        for (int i = 0; i < headersList.size(); i++, availableForExtend--) {
            int finalWidth = avgWidthOfCol + (availableForExtend > 0 ? 1 : 0);
            this.colWidthsList.add(finalWidth);
        }
        this.colAlignsList = new ArrayList<>();
        List<String> firstRow = rowsList.get(0);
        for (String cell : firstRow) {
            int alignMode;
            try {
                Long.parseLong(cell);
                alignMode = Block.DATA_MIDDLE_RIGHT;
            } catch (NumberFormatException e0) {
                try {
                    Integer.parseInt(cell);
                    alignMode = Block.DATA_MIDDLE_RIGHT;
                } catch (NumberFormatException e1) {
                    try {
                        Double.parseDouble(cell);
                        alignMode = Block.DATA_MIDDLE_RIGHT;
                    } catch (NumberFormatException e2) {
                        alignMode = Block.DATA_MIDDLE_LEFT;
                    }
                }
            }
            this.colAlignsList.add(alignMode);
        }
        headerHeight = 1;
        rowHeight = 1;
        gridMode = GRID_COLUMN;
    }

    public Table(Board board, int tableWidth, List<String> headersList, List<List<String>> rowsList, List<Integer> colWidthsList) {
        this(board, tableWidth, headersList, rowsList);
        if (colWidthsList.size() != headersList.size()) {
            throw new RuntimeException("Column width count(" + colWidthsList.size() + ") and header size(" + headersList.size() + ") are not equal");
        } else {
            this.colWidthsList = colWidthsList;
        }
    }

    public Table(Board board, int tableWidth, List<String> headersList, List<List<String>> rowsList, List<Integer> colWidthsList, List<Integer> colAlignsList) {
        this(board, tableWidth, headersList, rowsList, colWidthsList);
        if (colAlignsList.size() != headersList.size()) {
            throw new RuntimeException("Column align count(" + colAlignsList.size() + ") and header size(" + headersList.size() + ") are not equal");
        } else {
            this.colAlignsList = colAlignsList;
        }
    }

    public List<String> getHeadersList() {
        return headersList;
    }

    public Table setHeadersList(List<String> headersList) {
        this.headersList = headersList;
        return this;
    }

    public List<List<String>> getRowsList() {
        return rowsList;
    }

    public Table setRowsList(List<List<String>> rowsList) {
        this.rowsList = rowsList;
        return this;
    }

    public List<Integer> getColWidthsList() {
        return colWidthsList;
    }

    public Table setColWidthsList(List<Integer> colWidthsList) {
        if (colWidthsList.size() != headersList.size()) {
            throw new RuntimeException("Column width count(" + colWidthsList.size() + ") and header size(" + headersList.size() + ") are not equal");
        } else {
            this.colWidthsList = colWidthsList;
        }
        return this;
    }

    public List<Integer> getColAlignsList() {
        return colAlignsList;
    }

    public Table setColAlignsList(List<Integer> colAlignsList) {
        if (colAlignsList.size() != headersList.size()) {
            throw new RuntimeException("Column align count(" + colAlignsList.size() + ") and header size(" + headersList.size() + ") are not equal");
        } else {
            this.colAlignsList = colAlignsList;
        }
        return this;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public Table setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        return this;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public Table setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        return this;
    }

    public int getGridMode() {
        return gridMode;
    }

    public Table setGridMode(int gridMode) {
        if (gridMode == GRID_NON || gridMode == GRID_FULL || gridMode == GRID_COLUMN) {
            this.gridMode = gridMode;
        } else {
            throw new RuntimeException("Invalid grid mode. " + gridMode + " given.");
        }
        return this;
    }

    public Block tableToBlocks() {
        for (int i = 0; i < headersList.size(); i++) {
            String headerValue = headersList.get(i);
            int columnWidth = colWidthsList.get(i);
            Block block = new Block(board, columnWidth, headerHeight, headerValue);
            if (getGridMode() == GRID_NON) {
                block.allowGrid(false);
            } else {
                block.allowGrid(true);
            }
            int alignIndex = colAlignsList.get(i);
            block.setDataAlign(alignIndex);
            if (initialTableBlock == null) {
                initialTableBlock = block;
            } else {
                initialTableBlock.getMostRightBlock().setRightBlock(block);
            }
        }
        if (getGridMode() != GRID_COLUMN) {
            for (int i = 0; i < rowsList.size(); i++) {
                List<String> row = rowsList.get(i);
                Block rowStartingBlock = initialTableBlock.getMostBelowBlock();
                for (int j = 0; j < row.size(); j++) {
                    String rowValue = row.get(j);
                    int columnWidth = colWidthsList.get(j);
                    Block block = new Block(board, columnWidth, rowHeight, rowValue);
                    if (getGridMode() == GRID_NON) {
                        block.allowGrid(false);
                    } else {
                        block.allowGrid(true);
                    }
                    int alignIndex = colAlignsList.get(j);
                    block.setDataAlign(alignIndex);

                    if (rowStartingBlock.getBelowBlock() == null) {
                        rowStartingBlock.setBelowBlock(block);
                    } else {
                        rowStartingBlock.getBelowBlock().getMostRightBlock().setRightBlock(block);
                    }
                }
            }
        } else {            
            for (int i = 0; i < headersList.size(); i++) {
                String columnData = "";
                for (int j = 0; j < rowsList.size(); j++) {
                    String rowData = rowsList.get(j).get(i);
                    columnData = columnData.concat(rowData).concat("\n");
                }
                Block block = new Block(board, colWidthsList.get(i), rowsList.size(),columnData);
                int alignIndex = colAlignsList.get(i);
                    block.setDataAlign(alignIndex);
                if (initialTableBlock.getBelowBlock() == null) {
                    initialTableBlock.setBelowBlock(block);
                } else {
                    initialTableBlock.getBelowBlock().getMostRightBlock().setRightBlock(block);
                }
            }
        }
        return initialTableBlock;
    }
    
    public Table invalidate(){
        initialTableBlock = null;
        return this;
    }
}
class Board {
    
    protected boolean showBlockIndex;

    protected int boardWidth;

    private Block initialBlock;

    private List<Charr> charrs;

    private String preview;

    public static final int APPEND_RIGHT = 16;

    public static final int APPEND_BELOW = 17;

    public Board(int boardWidth) {
        this.boardWidth = boardWidth;
        this.charrs = new ArrayList<>();
        this.preview = "";
        this.showBlockIndex = false;
        Block.nextIndex = 0;
    }

    public Board setInitialBlock(Block initialBlock) {
        this.initialBlock = initialBlock;
        return this;
    }

    public boolean isBlockIndexShowing() {
        return showBlockIndex;
    }

    public void showBlockIndex(boolean showBlockIndex) {
        this.showBlockIndex = showBlockIndex;
    }        

    public Board appendTableTo(int appendableBlockIndex, int appendableDirection, Table table) {
        Block tableBlock = table.tableToBlocks();
        Block block = getBlock(appendableBlockIndex);
        if (appendableDirection == APPEND_RIGHT) {
            block.setRightBlock(tableBlock);
            rearranegCoordinates(block);
        } else if (appendableDirection == APPEND_BELOW) {
            block.setBelowBlock(tableBlock);
            rearranegCoordinates(block);
        } else {
            throw new RuntimeException("Invalid block appending direction given");
        }
        return this;
    }

    private void rearranegCoordinates(Block block) {
        Block rightBlock = block.getRightBlock();
        Block belowBlock = block.getBelowBlock();
        if (rightBlock != null && belowBlock == null) {
            block.setRightBlock(rightBlock);
            rearranegCoordinates(rightBlock);
        } else if (rightBlock == null && belowBlock != null) {
            block.setBelowBlock(belowBlock);
            rearranegCoordinates(belowBlock);
        } else if (rightBlock != null && belowBlock != null) {
            int rightIndex = rightBlock.getIndex();
            int belowIndex = belowBlock.getIndex();
            int blockIdDiff = rightIndex - belowIndex;
            if (blockIdDiff > 0) {
                if (blockIdDiff == 1) {
                    block.setRightBlock(rightBlock);
                    block.setBelowBlock(belowBlock);
                    rearranegCoordinates(rightBlock);
                    rearranegCoordinates(belowBlock);
                } else {
                    block.setRightBlock(rightBlock);
                    rearranegCoordinates(rightBlock);
                    block.setBelowBlock(belowBlock);
                    rearranegCoordinates(belowBlock);
                }
            } else if (blockIdDiff < 0) {
                blockIdDiff *= -1;
                if (blockIdDiff == 1) {
                    block.setBelowBlock(belowBlock);
                    block.setRightBlock(rightBlock);
                    rearranegCoordinates(belowBlock);
                    rearranegCoordinates(rightBlock);
                } else {
                    block.setBelowBlock(belowBlock);
                    rearranegCoordinates(belowBlock);
                    block.setRightBlock(rightBlock);
                    rearranegCoordinates(rightBlock);
                }
            }
        }
    }

    public Block getBlock(int blockIndex) {
        if (blockIndex >= 0) {
            return getBlock(blockIndex, initialBlock);
        } else {
            throw new RuntimeException("Block index cannot be negative. " + blockIndex + " given.");
        }
    }

    private Block getBlock(int blockIndex, Block block) {
        Block foundBlock = null;
        if (block.getIndex() == blockIndex) {
            return block;
        } else {
            if (block.getRightBlock() != null) {
                foundBlock = getBlock(blockIndex, block.getRightBlock());
            }
            if (foundBlock != null) {
                return foundBlock;
            }
            if (block.getBelowBlock() != null) {
                foundBlock = getBlock(blockIndex, block.getBelowBlock());
            }
            if (foundBlock != null) {
                return foundBlock;
            }
        }
        return foundBlock;
    }

    public Board build() {
        if (charrs.isEmpty()) {
            //rearranegCoordinates(initialBlock);
            buildBlock(initialBlock);
            dumpCharrsFromBlock(initialBlock);

            int maxY = -1;
            int maxX = -1;
            for (Charr charr : charrs) {
                int testY = charr.getY();
                int testX = charr.getX();
                if (maxY < testY) {
                    maxY = testY;
                }
                if (maxX < testX) {
                    maxX = testX;
                }
            }
            String[][] dataPoints = new String[maxY + 1][boardWidth];
            for (Charr charr : charrs) {
                String currentValue = dataPoints[charr.getY()][charr.getX()];
                String newValue = String.valueOf(charr.getC());
                if (currentValue == null || !currentValue.equals("+")) {
                    dataPoints[charr.getY()][charr.getX()] = newValue;
                }
            }

            for (String[] dataPoint : dataPoints) {
                for (String point : dataPoint) {
                    if (point == null) {
                        point = String.valueOf(Charr.S);
                    }
                    preview = preview.concat(point);
                }
                preview = preview.concat(String.valueOf(Charr.NL));
            }
        }

        return this;
    }

    public String getPreview() {
        build();
        return preview;
    }

    public Board invalidate() {
        invalidateBlock(initialBlock);
        charrs = new ArrayList<>();
        preview = "";
        return this;
    }

    private void buildBlock(Block block) {
        if (block != null) {
            block.build();
            buildBlock(block.getRightBlock());
            buildBlock(block.getBelowBlock());
        }
    }

    private void dumpCharrsFromBlock(Block block) {
        if (block != null) {
            charrs.addAll(block.getChars());
            dumpCharrsFromBlock(block.getRightBlock());
            dumpCharrsFromBlock(block.getBelowBlock());
        }
    }

    private void invalidateBlock(Block block) {
        if (block != null) {
            block.invalidate();
            invalidateBlock(block.getRightBlock());
            invalidateBlock(block.getBelowBlock());
        }
    }

}
final class Block {

    protected static int nextIndex = 0;

    private Board board;

    private final int index;

    private int width;

    private int height;

    private boolean allowGrid;

    private int blockAlign;

    public static final int BLOCK_LEFT = 1;

    public static final int BLOCK_CENTRE = 2;

    public static final int BLOCK_RIGHT = 3;

    private String data;

    private int dataAlign;

    public static final int DATA_TOP_LEFT = 4;

    public static final int DATA_TOP_MIDDLE = 5;

    public static final int DATA_TOP_RIGHT = 6;

    public static final int DATA_MIDDLE_LEFT = 7;

    public static final int DATA_CENTER = 8;

    public static final int DATA_MIDDLE_RIGHT = 9;

    public static final int DATA_BOTTOM_LEFT = 10;

    public static final int DATA_BOTTOM_MIDDLE = 11;

    public static final int DATA_BOTTOM_RIGHT = 12;

    private int x;

    private int y;

    private Block rightBlock;

    private Block belowBlock;

    private List<Charr> charrsList;

    private String preview;

    public Block(Board board, int width, int height) {
        this.board = board;
        if (width <= board.boardWidth) {
            this.width = width;
        } else {
            throw new RuntimeException("Block " + toString() + " exceeded the board width " + board.boardWidth);
        }
        this.height = height;
        this.allowGrid = true;
        this.blockAlign = BLOCK_LEFT;
        this.data = null;
        this.dataAlign = DATA_TOP_LEFT;
        this.x = 0;
        this.y = 0;
        this.rightBlock = null;
        this.belowBlock = null;
        this.charrsList = new ArrayList<>();
        this.preview = "";
        this.index = nextIndex;
        Block.nextIndex++;
    }

    public Block(Board board, int width, int height, String data) {
        this(board, width, height);
        this.data = data;
    }

    public Block(Board board, int width, int height, String data, Block rightBlock, Block belowBlock) {
        this(board, width, height, data);
        if (rightBlock != null) {
            rightBlock.setX(getX() + getWidth() + (isGridAllowed() ? 1 : 0));
            rightBlock.setY(getY());
            this.rightBlock = rightBlock;
        }
        if (belowBlock != null) {
            belowBlock.setX(getX());
            belowBlock.setY(getY() + getHeight() + (isGridAllowed() ? 1 : 0));
            this.belowBlock = belowBlock;
        }
    }

    protected int getIndex() {
        return index;
    }

    public int getWidth() {
        return width;
    }

    public Block setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Block setHeight(int height) {
        this.height = height;
        return this;
    }

    public boolean isGridAllowed() {
        return allowGrid;
    }

    public Block allowGrid(boolean allowGrid) {
        this.allowGrid = allowGrid;
        return this;
    }

    public int getBlockAlign() {
        return blockAlign;
    }

    public Block setBlockAlign(int blockAlign) {
        if (blockAlign == BLOCK_LEFT || blockAlign == BLOCK_CENTRE || blockAlign == BLOCK_RIGHT) {
            this.blockAlign = blockAlign;
        } else {
            throw new RuntimeException("Invalid block align mode. " + dataAlign + " given.");
        }
        return this;
    }

    public String getData() {
        return data;
    }

    public Block setData(String data) {
        this.data = data;
        return this;
    }

    public int getDataAlign() {
        return dataAlign;
    }

    public Block setDataAlign(int dataAlign) {
        if (dataAlign == DATA_TOP_LEFT || dataAlign == DATA_TOP_MIDDLE || dataAlign == DATA_TOP_RIGHT
                || dataAlign == DATA_MIDDLE_LEFT || dataAlign == DATA_CENTER || dataAlign == DATA_MIDDLE_RIGHT
                || dataAlign == DATA_BOTTOM_LEFT || dataAlign == DATA_BOTTOM_MIDDLE || dataAlign == DATA_BOTTOM_RIGHT) {
            this.dataAlign = dataAlign;
        } else {
            throw new RuntimeException("Invalid data align mode. " + dataAlign + " given.");
        }
        return this;
    }

    protected int getX() {
        return x;
    }

    protected Block setX(int x) {
        if (x + getWidth() + (isGridAllowed() ? 2 : 0) <= board.boardWidth) {
            this.x = x;
        } else {
            throw new RuntimeException("Block " + toString() + " exceeded the board width " + board.boardWidth);
        }
        return this;
    }

    protected int getY() {
        return y;
    }

    protected Block setY(int y) {
        this.y = y;
        return this;
    }

    public Block getRightBlock() {
        return rightBlock;
    }

    public Block setRightBlock(Block rightBlock) {
        if (rightBlock != null) {
            rightBlock.setX(getX() + getWidth() + (isGridAllowed() ? 1 : 0));
            rightBlock.setY(getY());
            this.rightBlock = rightBlock;
        }
        return this;
    }

    public Block getBelowBlock() {
        return belowBlock;
    }

    public Block setBelowBlock(Block belowBlock) {
        if (belowBlock != null) {
            belowBlock.setX(getX());
            belowBlock.setY(getY() + getHeight() + (isGridAllowed() ? 1 : 0));
            this.belowBlock = belowBlock;
        }
        return this;
    }

    protected Block invalidate() {
        charrsList = new ArrayList<>();
        preview = "";
        return this;
    }

    protected Block build() {
        if (charrsList.isEmpty()) {
            int ix = x;
            int iy = y;
            int blockLeftSideSpaces = -1;
            int additionalWidth = (isGridAllowed() ? 2 : 0);
            switch (getBlockAlign()) {
                case BLOCK_LEFT: {
                    blockLeftSideSpaces = 0;
                    break;
                }
                case BLOCK_CENTRE: {
                    blockLeftSideSpaces = (board.boardWidth - (ix + getWidth() + additionalWidth)) / 2 + (board.boardWidth - (ix + getWidth() + additionalWidth)) % 2;
                    break;
                }
                case BLOCK_RIGHT: {
                    blockLeftSideSpaces = board.boardWidth - (ix + getWidth() + additionalWidth);
                    break;
                }
            }
            ix += blockLeftSideSpaces;
            if (data == null) {
                data = toString();
            }
            String[] lines = data.split("\n");
            List<String> dataInLines = new ArrayList<>();
            if (board.showBlockIndex) {
                dataInLines.add("i = " + index);
            }
            for (String line : lines) {
                if (getHeight() > dataInLines.size()) {
                    dataInLines.add(line);
                } else {
                    break;
                }
            }
            for (int i = dataInLines.size(); i < getHeight(); i++) {
                dataInLines.add("");
            }
            for (int i = 0; i < dataInLines.size(); i++) {
                String dataLine = dataInLines.get(i);
                if (dataLine.length() > getWidth()) {
                    dataInLines.set(i, dataLine.substring(0, getWidth()));
                    if (i + 1 != dataInLines.size()) {
                        String prifix = dataLine.substring(getWidth(), dataLine.length());
                        String suffix = dataInLines.get(i + 1);
                        String combinedValue = prifix.concat((suffix.length() > 0 ? String.valueOf(Charr.S) : "")).concat(suffix);
                        dataInLines.set(i + 1, combinedValue);
                    }
                }
            }

            for (int i = 0; i < dataInLines.size(); i++) {
                if (dataInLines.remove("")) {
                    i--;
                }
            }

            int givenAlign = getDataAlign();
            int dataStartingLineIndex = -1;
            int additionalHeight = (isGridAllowed() ? 1 : 0);
            if (givenAlign == DATA_TOP_LEFT || givenAlign == DATA_TOP_MIDDLE || givenAlign == DATA_TOP_RIGHT) {
                dataStartingLineIndex = iy + additionalHeight;
            } else if (givenAlign == DATA_MIDDLE_LEFT || givenAlign == DATA_CENTER || givenAlign == DATA_MIDDLE_RIGHT) {
                dataStartingLineIndex = iy + additionalHeight + ((getHeight() - dataInLines.size()) / 2 + (getHeight() - dataInLines.size()) % 2);
            } else if (givenAlign == DATA_BOTTOM_LEFT || givenAlign == DATA_BOTTOM_MIDDLE || givenAlign == DATA_BOTTOM_RIGHT) {
                dataStartingLineIndex = iy + additionalHeight + (getHeight() - dataInLines.size());
            }
            int dataEndingLineIndex = dataStartingLineIndex + dataInLines.size();

            int extendedIX = ix + getWidth() + (isGridAllowed() ? 2 : 0);
            int extendedIY = iy + getHeight() + (isGridAllowed() ? 2 : 0);
            int startingIX = ix;
            int startingIY = iy;
            for (; iy < extendedIY; iy++) {
                for (; ix < extendedIX; ix++) {
                    boolean writeData;
                    if (isGridAllowed()) {
                        if ((iy == startingIY) || (iy == extendedIY - 1)) {
                            if ((ix == startingIX) || (ix == extendedIX - 1)) {
                                charrsList.add(new Charr(ix, iy, Charr.P));
                                writeData = false;
                            } else {
                                charrsList.add(new Charr(ix, iy, Charr.D));
                                writeData = false;
                            }
                        } else {
                            if ((ix == startingIX) || (ix == extendedIX - 1)) {
                                charrsList.add(new Charr(ix, iy, Charr.VL));
                                writeData = false;
                            } else {
                                writeData = true;
                            }
                        }
                    } else {
                        writeData = true;
                    }
                    if (writeData && (iy >= dataStartingLineIndex && iy < dataEndingLineIndex)) {
                        int dataLineIndex = iy - dataStartingLineIndex;
                        String lineData = dataInLines.get(dataLineIndex);
                        if (!lineData.isEmpty()) {
                            int dataLeftSideSpaces = -1;
                            if (givenAlign == DATA_TOP_LEFT || givenAlign == DATA_MIDDLE_LEFT || givenAlign == DATA_BOTTOM_LEFT) {
                                dataLeftSideSpaces = 0;
                            } else if (givenAlign == DATA_TOP_MIDDLE || givenAlign == DATA_CENTER || givenAlign == DATA_BOTTOM_MIDDLE) {
                                dataLeftSideSpaces = (getWidth() - lineData.length()) / 2 + (getWidth() - lineData.length()) % 2;
                            } else if (givenAlign == DATA_TOP_RIGHT || givenAlign == DATA_MIDDLE_RIGHT || givenAlign == DATA_BOTTOM_RIGHT) {
                                dataLeftSideSpaces = getWidth() - lineData.length();
                            }
                            int dataStartingIndex = (startingIX + dataLeftSideSpaces + (isGridAllowed() ? 1 : 0));
                            int dataEndingIndex = (startingIX + dataLeftSideSpaces + lineData.length() - (isGridAllowed() ? 0 : 1));
                            if (ix >= dataStartingIndex && ix <= dataEndingIndex) {
                                char charData = lineData.charAt(ix - dataStartingIndex);
                                charrsList.add(new Charr(ix, iy, charData));
                            }
                        }
                    }
                }
                ix = startingIX;
            }
        }
        return this;
    }

    protected List<Charr> getChars() {
        return this.charrsList;
    }

    public String getPreview() {
        build();
        if (preview.isEmpty()) {
            int maxY = -1;
            int maxX = -1;
            for (Charr charr : charrsList) {
                int testY = charr.getY();
                int testX = charr.getX();
                if (maxY < testY) {
                    maxY = testY;
                }
                if (maxX < testX) {
                    maxX = testX;
                }
            }
            String[][] dataPoints = new String[maxY + 1][board.boardWidth];
            for (Charr charr : charrsList) {
                dataPoints[charr.getY()][charr.getX()] = String.valueOf(charr.getC());
            }

            for (String[] dataPoint : dataPoints) {
                for (String point : dataPoint) {
                    if (point == null) {
                        point = String.valueOf(Charr.S);
                    }
                    preview = preview.concat(point);
                }
                preview = preview.concat(String.valueOf(Charr.NL));
            }
        }
        return preview;
    }

    public Block getMostRightBlock() {
        return getMostRightBlock(this);
    }

    private Block getMostRightBlock(Block block) {
        if (block.getRightBlock() == null) {
            return block;
        } else {
            return getMostRightBlock(block.getRightBlock());
        }
    }

    public Block getMostBelowBlock() {
        return getMostBelowBlock(this);
    }

    private Block getMostBelowBlock(Block block) {
        if (block.getBelowBlock() == null) {
            return block;
        } else {
            return getMostBelowBlock(block.getBelowBlock());
        }
    }

    @Override
    public String toString() {
        return index + " = [" + x + "," + y + "," + width + "," + height + "]";
    }

    @Override
    public boolean equals(Object block) {
        if (block == null) {
            return false;
        }
        if (!(block instanceof Block)) {
            return false;
        }
        Block b = (Block) block;
        return b.getIndex() == getIndex() && b.getX() == getX() && b.getY() == getY();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + this.index;
        hash = 43 * hash + this.width;
        hash = 43 * hash + this.height;
        hash = 43 * hash + (this.allowGrid ? 1 : 0);
        hash = 43 * hash + this.blockAlign;
        hash = 43 * hash + Objects.hashCode(this.data);
        hash = 43 * hash + this.dataAlign;
        hash = 43 * hash + this.x;
        hash = 43 * hash + this.y;
        hash = 43 * hash + Objects.hashCode(this.rightBlock);
        hash = 43 * hash + Objects.hashCode(this.belowBlock);
        return hash;
    }
}

public class TableViewFormat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> headersList = Arrays.asList("NAME", "GENDER", "MARRIED", "AGE", "SALARY($)");
		List<List<String>> rowsList = Arrays.asList(
		        Arrays.asList("Eddy", "Male", "No", "23", "1200.27"),
		        Arrays.asList("Libby", "Male", "No", "17", "800.50"),
		        Arrays.asList("Rea", "Female", "No", "30", "10000.00"),
		        Arrays.asList("Deandre", "Female", "No", "19", "18000.50"),
		        Arrays.asList("Alice", "Male", "Yes", "29", "580.40"),
		        Arrays.asList("Alyse", "Female", "No", "26", "7000.89"),
		        Arrays.asList("Venessa", "Female", "No", "22", "100700.50")
		);
		//bookmark 1
		Board board = new Board(75);
		String tableString = board.setInitialBlock(new Table(board, 75, headersList, rowsList).tableToBlocks()).build().getPreview();
		System.out.println(tableString);
	}

}
