package aim.knapsack;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KnapsackConfig {

    private static KnapsackConfig instance;

    @Value("item")
    private List<Item> items = new ArrayList<>();

    @Value("max.total.weight")
    private int maxTotalWeight;

    @Value(("iterations.number"))
    private int numberOfIterations;

    // simulated annealing

    @Value("initial.temperature")
    private double initialTemperature;

    @Value("minimal.temperature")
    private double minimalTemperature;

    @Value("cooling.rate")
    private double coolingRate;

    // genetic
    @Value("population.size")
    private int populationSize;

    @Value("mutation.probability")
    private double mutationsProbability;

    @Value("recombination.probability")
    private double recombinationProbability;

    @Value("selection.type")
    private SelectionType selectionType;

    @Value("crossing.type")
    private CrossingType crossingType;

    public int totalItemsValue() {
        return items.stream()
                .mapToInt(Item::getValue)
                .sum();
    }

    @SuppressWarnings("WeakerAccess")
    public static KnapsackConfig getInstance() {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    @SneakyThrows
    private static KnapsackConfig init() {
        KnapsackConfig config = new KnapsackConfig();

        final Properties properties = new Properties();
        properties.load(KnapsackConfig.class.getResourceAsStream("knapsack.properties"));
        for (Field field : KnapsackConfig.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Value.class)) {
                continue;
            }
            field.setAccessible(true);
            String propertyName = field.getAnnotation(Value.class).value();
            String propertyValue = properties.getProperty(propertyName);
            Class<?> type = field.getType();
            if (int.class.equals(type)) {
                field.setInt(config, Integer.parseInt(propertyValue));
            } else if (double.class.equals(type)) {
                field.setDouble(config, Double.parseDouble(propertyValue));
            } else if (type.isEnum()) {
                //noinspection unchecked
                field.set(config, Enum.valueOf((Class<Enum>) type, propertyValue));
            } else {
                List<Item> items = new ArrayList<>();
                int i = 1;
                String value;
                while ((value = properties.getProperty(String.format(propertyName + "%d", i))) != null) {
                    items.add(toItem(value));
                    i++;
                }
                field.set(config, items);
            }
        }

        return config;
    }

    private static Item toItem(String value) {
        String[] split = value.split("-");
        return Item.builder()
                .value(Integer.parseInt(split[ 0 ]))
                .weight(Integer.parseInt(split[ 1 ]))
                .build();
    }
}
