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

    private List<EventPlan> allPlans;
    private LongSparseArray<List<EventPlan>> allDatePlans;

    private EventPlanProvider() {
        allPlans = EventPlanDatabaseMaster.getAllEvent();
        allDatePlans = new LongSparseArray<>();
        for (EventPlan plan : allPlans) {
            add2AllDateCache(plan);
        }
    }

    public static EventPlanProvider get() {
        return SingleInstanceHolder.holder;
    }


    public List<EventPlan> getAllPlans() {
        return allPlans;
    }

    public void insertOrUpdate(@NonNull EventPlan plan) {
        //1. insert or update
        final boolean isInsert = plan.getId() < 1;
        EventPlanDatabaseMaster.insertOrUpdate(plan);
        if (isInsert) {
            allPlans.add(plan);
            add2AllDateCache(plan);
        }
    }


    public void deleteEvent(EventPlan plan) {
        allPlans.remove(plan);
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
        for (EventPlan plan : allPlans) {
            if (plan.getId() == eventId)
                return plan;
        }
        return null;
    }


}
