package snippets.test;

import java.util.Calendar;

import snippets.PersonNameMatcher;

public class TestPersonNameMatcher implements PersonNameMatcher {
	private String lastName;
	private String firstName;
	private String gender;
	private Calendar dateOfBirth;
	private String weakMatchValue;

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return firstName;
	}

	@Override
	public String getGender() {
		return gender;
	}

	@Override
	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	@Override
	public boolean weakMatch(Object candidate) {
		return this.weakMatchValue.equals(((TestPersonNameMatcher) candidate)
				.getWeakMatchValue());
	}

	/**
	 * @return the weakMatchValue
	 */
	public String getWeakMatchValue() {
		return weakMatchValue;
	}

	/**
	 * @param weakMatchValue
	 *            the weakMatchValue to set
	 */
	public void setWeakMatchValue(String weakMatchValue) {
		this.weakMatchValue = weakMatchValue;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((weakMatchValue == null) ? 0 : weakMatchValue.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TestPersonNameMatcher))
			return false;
		TestPersonNameMatcher other = (TestPersonNameMatcher) obj;
		if (dateOfBirth == null) {
			if (other.dateOfBirth != null)
				return false;
		} else if (!dateOfBirth.equals(other.dateOfBirth))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (weakMatchValue == null) {
			if (other.weakMatchValue != null)
				return false;
		} else if (!weakMatchValue.equals(other.weakMatchValue))
			return false;
		return true;
	}

}
