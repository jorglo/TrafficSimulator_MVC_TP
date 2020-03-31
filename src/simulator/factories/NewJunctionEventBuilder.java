package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {	
	
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory,
			Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected NewJunctionEvent createTheInstance(JSONObject data) {
		
		LightSwitchingStrategy lss = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dqs = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));
		
		NewJunctionEvent first = new NewJunctionEvent(data.getInt("time"), data.getString("id"), 
				lss, dqs, data.getJSONArray("coor").getInt(0), data.getJSONArray("coor").getInt(1));
		
		return first;
	}

}
