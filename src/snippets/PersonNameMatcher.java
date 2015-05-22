package snippets;

import java.util.Calendar;

public interface PersonNameMatcher {

	/**
	 * @return the lastName
	 */
	public String getLastName();

	/**
	 * @return the firstName
	 */
	public String getFirstName();

	/**
	 * @return the gender
	 */
	public String getGender();

	/**
	 * @return the dateOfBirth
	 */
	public Calendar getDateOfBirth();

	/**
	 * User defined weak match criteria. This could be matching on middle
	 * initial or other fields not visible through the PersonNameMatcher interface
	 * 
	 * @param candidate
	 * @return true if there is a weak watch
	 */
	public boolean weakMatch(Object candidate);
}
