## 算法思路

![Screen Shot 2019-03-17 at 12.33.49 PM](/Users/s1mple/Desktop/Screen Shot 2019-03-17 at 12.33.49 PM.png)

- 计算 **9 段和最大**所需员工总人数，此人数必定满足**上一休二**且**早中晚均衡**的规则，生成该大小的 staff_list （LinkedList）；

- 每天抽象成早、中、晚 3 个容器；staff_list 中从头开始遍历，如果当前 staff 满足早中晚均衡条件，则将该 staff 加入容器，并将该 staff 从链表中取出放到队尾；

- Staff 维护一个早中晚阶段的标志（periodFlags，Period 的 Set）。每次任命前判断该员工的 periodFlags 是否包含了**当前时段**。如果包含，则不满足早中晚均衡，跳过该员工迭代下一个。如果不包含，则任命该员工，并在 periodFlag 中加入当前时段。如果 periodFlags 大小等于 3（即包含了早、中、晚三个时段），则将 periodFlags 清空。

## 建模

### DailyPlan & Container

每日计划类，包含日期 `date`，以及抽象的早中晚 Container 内部类。
Container 类包含：

- `period`，早中晚阶段的枚举类
- `number`，该阶段需要的员工人数，如第2日早上需要5人
- `assignedStaffs`，已经任命的员工集合

主要的任命逻辑在 `assign(staffs)` 方法中，步骤如下：

1. 从 staffs 该 LinkedList 中从头遍历，**检查员工如果被任命后是否满足早中晚均衡的条件**，如果不满足跳到下一个员工
2. 如果满足条件，将该员工取出，加入 assignedStaffs 中并更新员工的 workPlans 记录，并将该员工放入 staffs 的队尾。退出循环，任命下一个满足条件的员工
3. 直到 assignedStaffs 大小等于 number（全部任命完成），结束整个循环

```java
public class DailyPlan {
    private int date;
    private Container morning;
    private Container afternoon;
    private Container night;

    public void assign(LinkedList<Staff> staffs) {
        morning().assign(staffs);
        afternoon().assign(staffs);
        night().assign(staffs);
    }

    public class Container {
        private Period period;
        private int number;
        private Set<Staff> assignedStaffs;

        public void assign(LinkedList<Staff> staffs) {
            while (assignedStaffs.size() < number) {
                for (int i = 0; i < staffs.size(); i++) {
                    if (staffs.get(i).isBalancedAfterAssign(period)) {
                        Staff staff = staffs.remove(i);
                        assignedStaffs.add(staff);
                        staff.addWorkPlan(date, period);
                        staffs.addLast(staff);
                        break;
                    }
                }
            }
        }
    }
}
```

### Staff

员工类，其中：
- `workPlans` 记录员工都上了哪些天的什么时段的班；
- `periodFlags` 记录员工当前上的时段的标志，在 `addWorkPlan()` 中如果 `periodFlags` 的大小等于 3（亦即包含了早中晚），就将该标识清空（保证 3 个一清空）；
- `isBalancedAfterAssign(period)` 验证如果**安排当前员工工作后是否满足早中晚均衡**。不满足该条件则跳过改员工，继续安排下一个；

```java
public class Staff {
    private final int id;
    private final List<WorkPlan> workPlans;
    private final Set<Period> periodFlags;

    public boolean isBalancedAfterAssign(Period period) {
        return !periodFlags.contains(period);
    }

    public void addWorkPlan(int date, Period period) {
        if (!isBalancedAfterAssign(period)) {
            throw new RuntimeException();
        }

        workPlans.add(new WorkPlan(date, period));
        periodFlags.add(period);
        if (periodFlags.size() == 3) {
            periodFlags.clear();
        }
    }
}
```

### Scheduling

排班调度主体类，包含：

- `CYCLE`，周期，上一休二所以为 3
- `dailyPlans`，每日计划的列表，包含每天什么时段需要多少人
- `staffs`，算法思路中描述的 staff_list 链表

排班逻辑在 `schedule()` 方法中，步骤如下：

1. 计算需要多少员工，根据 **9 段之和最大**得出
2. 初始化 staff_list
3. 每日安排员工上班

```java
public class Scheduling {
    private static final int CYCLE = 3;
    private final List<DailyPlan> dailyPlans;
    private LinkedList<Staff> staffs;

    public void schedule() {
        int expectStaffs = expectStaffs();
        staffs = initStaffList(expectStaffs);
        for (DailyPlan dailyPlan : dailyPlans) {
            dailyPlan.assign(staffs);
        }
    }
}
```

## 结果展示

1. 先随机生成 31 的计划，每个阶段人员波动从 2 到 10
2. 调度后将 staffs 中每个员工的排班情况打印出来

```java
@Test
public void should_schedule_staff_correctly() {
    DailyPlansGenerator generator = new DailyPlansGenerator(2, 10);
    List<DailyPlan> dailyPlans = generator.generate(31);
    System.out.println(generator);

    Scheduling scheduling = new Scheduling(dailyPlans);
    scheduling.schedule();
    for (Staff staff : scheduling.staffs()) {
        System.out.println(staff);
    }
}
```

![Screen Shot 2019-03-17 at 12.22.19 PM](/Users/s1mple/Desktop/Screen Shot 2019-03-17 at 12.22.19 PM.png)

![Screen Shot 2019-03-17 at 12.22.32 PM](/Users/s1mple/Desktop/Screen Shot 2019-03-17 at 12.22.32 PM.png)

![image-20190317123311616](/Users/s1mple/Library/Application Support/typora-user-images/image-20190317123311616.png)