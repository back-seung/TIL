package generic;

class Fruit { public String toString() { return "Fruit"; }}
class Apple extends Fruit { public String toString() { return "Apple"; }}
class Grape extends Fruit { public String toString() { return "Grape"; }}
class Toy { public String toString() { return "Toy"; }}

class FruitBoxEx1 {
    public static void main(String[] args) {
        NewBox<Fruit> fruitBox = new NewBox<>();
        NewBox<Toy> toyBox = new NewBox<>();
//        Box<Grape> grapeBox = new Box<Apple>(); 에러. 타입 불일치

        fruitBox.add(new Fruit());
        fruitBox.add(new Apple());
        fruitBox.add(new Grape());
//        fruitBox.add(new Toy()); 에러 Box<Fruit>에는 Fruit, Apple, Grape만 담을 수 있음

        toyBox.add(new Toy());
//        toyBox.add(new Apple()); 에러 Box<Toy>에는 Toy만 담을 수 있음

        System.out.println(fruitBox);
        System.out.println(toyBox);
    }
}

