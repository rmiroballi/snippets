package snippets.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import snippets.NameMatcher;
import snippets.PersonNameMatcher;

public class NameMatcherTest {

	@Test
	public void fuzzyNameMatch() {
		NameMatcher matcher = new NameMatcher();

		Calendar candidateDOB1 = Calendar.getInstance();
		candidateDOB1.set(1957, 3, 24); // 1957-04-24
		Calendar candidateDOB2 = Calendar.getInstance();
		candidateDOB2.set(1962, 4, 25); // 1962-05-25
		Calendar candidateDOB3 = Calendar.getInstance();
		candidateDOB3.set(1972, 7, 4); // 1972-08-04
		Calendar candidateDOB4 = Calendar.getInstance();
		candidateDOB4.set(2002, 9, 15); // 2002-10-15
		Calendar candidateDOB5 = Calendar.getInstance();
		candidateDOB5.set(2015, 1, 10); // 2015-01-10
		Calendar personDOB1 = Calendar.getInstance();
		personDOB1.set(1957, 4, 24); // 1957-05-24
		Calendar personDOB2 = Calendar.getInstance();
		personDOB2.set(1972, 7, 4); // 1972-08-04
		Calendar personDOB3 = Calendar.getInstance();
		personDOB3.set(2000, 9, 15); // 2000-10-15
		Calendar personDOB4 = Calendar.getInstance();
		personDOB4.set(2015, 1, 10); // 2015-01-10

		TestPersonNameMatcher person = new TestPersonNameMatcher();
		person.setFirstName("");
		person.setLastName("");
		person.setGender("");
		person.setDateOfBirth(null);
		person.setWeakMatchValue("012345");

		String alternateLastName = "CAGEY";

		ArrayList<PersonNameMatcher> candidates = new ArrayList<PersonNameMatcher>(
				0);
		TestPersonNameMatcher candidate1 = new TestPersonNameMatcher();
		candidate1.setFirstName("NICHOLAS");
		candidate1.setLastName("CAGEY");
		candidate1.setGender("M");
		candidate1.setDateOfBirth(candidateDOB1);
		candidate1.setWeakMatchValue("012345");
		candidates.add(candidate1);

		TestPersonNameMatcher candidate2 = new TestPersonNameMatcher();
		candidate2.setFirstName("JOANNE");
		candidate2.setLastName("CAGEY");
		candidate2.setGender("F");
		candidate2.setDateOfBirth(candidateDOB2);
		candidate2.setWeakMatchValue("012345");
		candidates.add(candidate2);

		TestPersonNameMatcher candidate3 = new TestPersonNameMatcher();
		candidate3.setFirstName("LAURA");
		candidate3.setLastName("MCNICKOLS");
		candidate3.setGender("F");
		candidate3.setDateOfBirth(candidateDOB3);
		candidate3.setWeakMatchValue("012345");
		candidates.add(candidate3);

		TestPersonNameMatcher candidate4 = new TestPersonNameMatcher();
		candidate4.setFirstName("NICHOLS");
		candidate4.setLastName("MCNICKOLS-CAGEY");
		candidate4.setGender("M");
		candidate4.setDateOfBirth(candidateDOB4);
		candidate4.setWeakMatchValue("012345");
		candidates.add(candidate4);

		TestPersonNameMatcher candidate5 = new TestPersonNameMatcher();
		candidate5.setFirstName("MICHELLE");
		candidate5.setLastName("CAGEY");
		candidate5.setGender("F");
		candidate5.setDateOfBirth(candidateDOB5);
		candidate5.setWeakMatchValue("012345");
		candidates.add(candidate5);

		// Birthday month differs. (one back)
		person.setDateOfBirth(personDOB1);
		person.setFirstName("NICHOLAS");
		person.setLastName("CAGEY");
		person.setGender("M");
		List<PersonNameMatcher> found = matcher.fuzzyNameMatch(person,
				candidates, alternateLastName, 8, 5);
		ArrayList<PersonNameMatcher> actuals = new ArrayList<PersonNameMatcher>(
				0);
		ArrayList<PersonNameMatcher> expecteds = new ArrayList<PersonNameMatcher>(
				0);
		expecteds.add(candidate1);
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

		// Birthday way off. (one back)
		person.setDateOfBirth(personDOB2);
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				8, 5);
		actuals.clear();
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(expecteds.toArray(new PersonNameMatcher[0]),
				actuals.toArray(new PersonNameMatcher[0]));

		// First name way off. (one back)
		person.setDateOfBirth(personDOB4);
		person.setFirstName("BABYGIRL");
		person.setLastName("CAGEY");
		person.setGender("F");
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				8, 5);
		actuals.clear();
		expecteds.clear();
		expecteds.add(candidate5);
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

		// Hyphenated name off. (one back)
		person.setDateOfBirth(personDOB3);
		person.setFirstName("NICHOLS");
		person.setLastName("CAGEY");
		person.setGender("M");
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				8, 5);
		actuals.clear();
		expecteds.clear();
		expecteds.add(candidate4);
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

		// Completely wrong name (none back)
		person.setDateOfBirth(personDOB3);
		person.setFirstName("ALBERT");
		person.setLastName("WASHINGTON");
		person.setGender("M");
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				8, 5);
		actuals.clear();
		expecteds.clear();
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

		// Strict search (one back)
		person.setDateOfBirth(candidateDOB1);
		person.setFirstName("NICHOLAS");
		person.setLastName("CAGEY");
		person.setGender("M");
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				0, 5);
		actuals.clear();
		expecteds.clear();
		expecteds.add(candidate1);
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

		// Strict search (none back)
		person.setDateOfBirth(candidateDOB1);
		person.setFirstName("NICOLAS");
		person.setLastName("CAGEY");
		person.setGender("M");
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				0, 5);
		actuals.clear();
		expecteds.clear();
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

		// VERY lenient search (Four back)
		person.setDateOfBirth(personDOB2);
		person.setFirstName("NICHOLS");
		person.setLastName("CAGEY");
		person.setGender("M");
		found = matcher.fuzzyNameMatch(person, candidates, alternateLastName,
				20, 5);
		actuals.clear();
		expecteds.clear();
		expecteds.add(candidate4);
		expecteds.add(candidate1);
		expecteds.add(candidate5);
		expecteds.add(candidate2);
		for (PersonNameMatcher foundPerson : found) {
			actuals.add((TestPersonNameMatcher) foundPerson);
		}

		Assert.assertArrayEquals(
				expecteds.toArray(new TestPersonNameMatcher[0]),
				actuals.toArray(new TestPersonNameMatcher[0]));

	}
}
