package week_11;public class EnumEx1 {    public static void main(String[] args) {        Direction d1 = Direction.EAST;        Direction d2 = Direction.valueOf("WEST");        Direction d3 = Direction.valueOf("EAST");        System.out.println("d1 : " + d1);        System.out.println("d2 : " + d2);        System.out.println("d3 : " + d3);        System.out.println("d1 == d2 ? " + (d1 == d2));        System.out.println("d1 == d3 ? " + (d1 == d3));//        System.out.println("d1 > d2 ? " + (d1 > d2)); 에러.        System.out.println("d1.equals(d2)" + (d1.equals(d2)));        System.out.println("d1.equals(d3)" + (d1.equals(d3)));        System.out.println("d1.compareTo(d2)" + d1.compareTo(d2));        System.out.println("d1.compareTo(d3)" + d1.compareTo(d3));        switch (d1) {            case EAST :                System.out.println("Direction is EAST");                break;            case WEST :                System.out.println("Direction is WEST");                break;            case SOUTH :                System.out.println("Direction is SOUTH");                break;            case NORTH :                System.out.println("Direction is NORTH");                break;            default :                System.out.println("Invalid Direction");                break;        }        Direction[] dirArr = Direction.values();        for (Direction direction : dirArr) {            System.out.printf("%s = %d%n", direction.name(), direction.ordinal());        }    }}