package assignment1.eventplan.db.master;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import assignment1.eventplan.entity.EventPlan;
import assignment1.eventplan.utils.DateUtil;

public class EventPlanProvider {


    static class SingleInstanceHolder {
        static final EventPlanProvider holder = new EventPlanProvider();
    }

    private final LongSparseArray<EventPlan> allEvenPlans;
    private final LongSparseArray<List<EventPlan>> allDatePlans;

    private EventPlanProvider() {
        allDatePlans = new LongSparseArray<>();
        EventPlanDatabaseMaster.getAllEvent(allEvenPlans = new LongSparseArray<>());
        for (int i = 0, size = allEvenPlans.size(); i < size; i++) {
            add2AllDateCache(allEvenPlans.valueAt(i));
        }
    }

    public static EventPlanProvider get() {
        return SingleInstanceHolder.holder;
    }


    public List<EventPlan> getAllPlans() {
        final int size = allEvenPlans.size();
        List<EventPlan> allPlans = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            allPlans.add(allEvenPlans.valueAt(i));
        }
        return allPlans;
    }

    public void insertOrUpdate(@NonNull EventPlan plan) {
        //1. insert or update
        final boolean isInsert = plan.getId() < 1;
        EventPlanDatabaseMaster.insertOrUpdate(plan);
        if (isInsert) {
            allEvenPlans.put(plan.getId(), plan);
            add2AllDateCache(plan);
        }
    }


    public void deleteEvent(EventPlan plan) {
        allEvenPlans.remove(plan.getId());
        removeFromAllDateCache(plan);
        EventPlanDatabaseMaster.deleteEvent(plan);

    }

    private void removeFromAllDateCache(EventPlan plan) {
        List<EventPlan> sub = allDatePlans.get(plan.getDate());
        if (null != sub) {
            sub.remove(plan);
            if (sub.isEmpty()) {
                allDatePlans.remove(plan.getDate());
            }
        }
    }

    private void add2AllDateCache(EventPlan plan) {
        List<EventPlan> sub = allDatePlans.get(plan.getDate());
        if (null == sub) {
            sub = new ArrayList<>();
            allDatePlans.put(plan.getDate(), sub);
        }
        sub.add(plan);
    }

    public static boolean hasPlan(String s) {
        return !get().findAllPlanByDate(s).isEmpty();
    }

    /**
     * @param date {@link EventPlan#formatPattern}
     * @return
     */
    @NonNull
    public List<EventPlan> findAllPlanByDate(String date) {
        List<EventPlan> plans = allDatePlans.get(DateUtil.parseDateTimeMillis(date));
        if (null == plans)
            return Collections.emptyList();
        return plans;

    }

    @Nullable
    public EventPlan getEventById(long eventId) {
        return allEvenPlans.get(eventId);
    }


}
