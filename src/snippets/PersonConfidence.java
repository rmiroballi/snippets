package snippets;

public class PersonConfidence implements Comparable<PersonConfidence> {

	private PersonNameMatcher person;
	private Integer confidence;

	public PersonConfidence(PersonNameMatcher member, Integer confidence) {
		super();
		this.setMember(member);
		this.setConfidence(confidence);
	}

	public PersonNameMatcher getPerson() {
		return person;
	}

	public void setMember(PersonNameMatcher person) {
		this.person = person;
	}

	public Integer getConfidence() {
		return confidence;
	}

	public void setConfidence(Integer confidence) {
		this.confidence = confidence;
	}

	@Override
	public int compareTo(PersonConfidence o) {
		if (o != null && confidence != null && o.getConfidence() != null) {
			return confidence.compareTo(o.getConfidence());
		}
		return 0;

	}
}
