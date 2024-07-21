package nl.thebathduck.minestom.blocks.state;


import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.utils.Direction;

public class States {

    public static final String HALF = "half";
    public static final String FACING = "facing";
    public static final String FACE = "face";
    public static final String SHAPE = "shape";
    public static final String WATERLOGGED = "waterlogged";
    public static final String NORTH = "north";
    public static final String EAST = "east";
    public static final String SOUTH = "south";
    public static final String WEST = "west";
    public static final String UP = "up";

    public static BlockFace getHalf(Block block) {
        if (block.getProperty(HALF) == null) return BlockFace.BOTTOM;
        return BlockFace.valueOf(block.getProperty(HALF).toUpperCase());
    }

    public static BlockFace getFacing(Block block) {
        if (block.getProperty(FACING) == null) return BlockFace.NORTH;
        return BlockFace.valueOf(block.getProperty(FACING).toUpperCase());
    }

    public static Direction getDirection(Block block) {
        if (block.getProperty(FACE) == null) return Direction.NORTH;
        return switch (block.getProperty(FACE)) {
            case "ceiling" -> Direction.DOWN;
            case "floor" -> Direction.UP;
            default -> getFacing(block).toDirection();
        };
    }

    public static Direction rotateYCounterclockwise(Direction direction) {
        return switch (direction.ordinal()) {
            case 2 -> Direction.WEST;
            case 5 -> Direction.NORTH;
            case 3 -> Direction.EAST;
            case 4 -> Direction.SOUTH;
            default -> throw new IllegalStateException("Unable to rotate " + direction);
        };
    }
    public static Direction rotateYClockwise(Direction direction) {
        return switch (direction.ordinal()) {
            case 2 -> Direction.EAST;
            case 5 -> Direction.SOUTH;
            case 3 -> Direction.WEST;
            case 4 -> Direction.NORTH;
            default -> throw new IllegalStateException("Unable to rotate " + direction);
        };
    }

    public static Axis getAxis(Direction direction) {
        return switch (direction) {
            case DOWN, UP -> Axis.Y;
            case NORTH, SOUTH -> Axis.Z;
            case WEST, EAST -> Axis.X;
        };
    }

    public enum Axis {
        X,
        Y,
        Z
    }

}