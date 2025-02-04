package bp.schedule;

import java.util.Map;

import bp.schedule.BPScheduleTarget.BPScheduleTargetParams;
import bp.util.DateUtil;
import bp.util.ObjUtil;

public class BPScheduleCalendar extends BPScheduleBase
{
	protected volatile long m_nexttime;
	protected volatile int m_interval;
	protected volatile int m_unit;
	protected volatile boolean m_runonce;

	protected volatile long m_starttime;

	public BPScheduleCalendar()
	{
	}

	public void setMappedData(Map<String, Object> data)
	{
		super.setMappedData(data);
		long starttime = ObjUtil.toLong(data.get("starttime"), null);
		m_starttime = starttime;
		int interval = ObjUtil.toInt(data.get("interval"), null);
		m_unit = ObjUtil.toInt(data.get("unit"), null);
		m_runonce = ObjUtil.toBool(data.get("runonce"), false);

		m_nexttime = starttime;
		m_interval = interval;

		long ct = System.currentTimeMillis();
		if (ct >= starttime)
		{
			m_nexttime = DateUtil.ceilDate(starttime, ct, m_interval, m_unit);
		}
	}

	public Map<String, Object> getMappedData()
	{
		Map<String, Object> rc = super.getMappedData();
		rc.put("starttime", m_starttime);
		rc.put("interval", m_interval);
		rc.put("unit", m_unit);
		rc.put("runonce", m_runonce);
		return rc;
	}

	public void check(BPScheduler scheduler, Object... datas)
	{
		if (!m_enabled)
			return;
		long nexttime = m_nexttime;
		long ct = System.currentTimeMillis();
		if (ct >= nexttime)
		{
			m_nexttime = DateUtil.ceilDate(m_nexttime, ct, m_interval, m_unit);
			if (m_runonce)
				m_enabled = false;
			runInner(ct, new BPScheduleTargetParams(scheduler, this, null));
		}
	}

	public void prepare()
	{
		m_nexttime = DateUtil.ceilDate(m_nexttime, System.currentTimeMillis(), m_interval, m_unit);
	}

	public void run()
	{
		long ct = System.currentTimeMillis();
		m_nexttime = DateUtil.ceilDate(m_nexttime, ct, m_interval, m_unit);
		runInner(ct, null);
	}

	public final static class BPScheduleFactoryCalendar implements BPScheduleFactory
	{
		public String getName()
		{
			return "Calendar";
		}

		public BPSchedule create(Map<String, Object> params)
		{
			BPSchedule rc = new BPScheduleCalendar();
			rc.setMappedData(params);
			return rc;
		}

		public Class<? extends BPSchedule> getScheduleClass()
		{
			return BPScheduleCalendar.class;
		}
	}
}
