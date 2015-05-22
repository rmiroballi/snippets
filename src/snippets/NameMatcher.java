package snippets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * Do a fuzzy match on the person against the given source persons. <br>
 * <b>Important</b> <br>
 * This depends on org.apache.commons.lang3.StringUtils and
 * org.apache.commons.lang3.time.DateUtils <br>
 * The algorithm will look for the most 'perfect' match. Perfect is defined by
 * First, Last, Gender and Date of Birth all being equal between the candidate
 * and the person. <br>
 * Confidence is ranked from 0 (perfect) to MAX Integer (everybody matches) <br>
 * 
 * This takes into account the following: <li>Hyphenated names (separated by '-'
 * and ' ') <li>Swapped days on DOB (e.g. 12 instead of 21) <li>Possible
 * Alternate Last name.<br>
 * It also provides (through the {@link PersonNameMatcher} interface) the
 * ability to do a user defined weak match.
 * 
 * @author Robert Miroballi (rmiroballi@nvisia.com)
 *
 */
public class NameMatcher {

	/**
	 * Confidence Thresholds for triggering a match. If the current confidence
	 * level is less than the threshold, a match will be attempted.
	 * <ul>
	 * <li>yearOfBirthThreshold = 5
	 * <li>dayOfBirthThreshold = 5
	 * <li>weakMatchThreshold = 5
	 * </ul>
	 */
	private Integer yearOfBirthThreshold = 5;
	private Integer dayOfBirthThreshold = 5;
	private Integer weakMatchThreshold = 5;

	/**
	 * Confidence Increment: how much to increase the confidence (defaults to 2)
	 */
	private Integer confidentIncrement = 2;

	/**
	 * 
	 */
	public NameMatcher() {
		super();
	}

	/**
	 * Adjust the thresholds and differences used to match. Null, negative or
	 * zero values will use the defaults.
	 * 
	 * @param yearOfBirthThreshold
	 * @param dayOfBirthThreshold
	 * @param weakMatchThreshold
	 * @param confidentIncrement
	 */
	public NameMatcher(Integer yearOfBirthThreshold,
			Integer dayOfBirthThreshold, Integer weakMatchThreshold,
			Integer hyphenatedDifference, Integer dobGenderDifference,
			Integer confidentIncrement) {
		super();
		if (yearOfBirthThreshold != null && yearOfBirthThreshold > 0) {
			this.yearOfBirthThreshold = yearOfBirthThreshold;
		}
		if (dayOfBirthThreshold != null && dayOfBirthThreshold > 0) {
			this.dayOfBirthThreshold = dayOfBirthThreshold;
		}
		if (weakMatchThreshold != null && weakMatchThreshold > 0) {
			this.weakMatchThreshold = weakMatchThreshold;
		}
		if (confidentIncrement != null && confidentIncrement > 0) {
			this.confidentIncrement = confidentIncrement;
		}
	}

	/**
	 * Do a match on the person against the given source persons. Only return
	 * the best found match. (0 == perfect match) <br>
	 * A perfect match is DOB, Last, First, Gender <br>
	 * <b>Important</b> <br>
	 * This depends on org.apache.commons.lang3.StringUtils and
	 * org.apache.commons.lang3.time.DateUtils
	 * 
	 * @param person
	 *            - {@link PersonNameMatcher} the person to match.
	 * @param sourcePersons
	 *            - List<{@link PersonNameMatcher}> of Persons to match against
	 * @param alternateLastName
	 *            - A last name that may be used in place of another last name
	 *            (Common when one spouse doesn't change last name, and children
	 *            get the other's name)
	 * @param ceiling
	 *            Highest level of uncertainty allowed. 0 is a perfect match.
	 *            (negative numbers default to 0)
	 * @param maxReturns
	 *            Maximum number of persons to return (0 or less defaults to 1).
	 * @return List<{@link PersonNameMatcher}> person that may match the given
	 *         PersonNameMatcher.
	 */
	public List<PersonNameMatcher> fuzzyNameMatch(PersonNameMatcher person,
			List<PersonNameMatcher> sourcePersons, String alternateLastName,
			int ceiling, int maxReturns) {
		ArrayList<PersonConfidence> tempResults = new ArrayList<PersonConfidence>(
				0);
		if (maxReturns < 1) {
			maxReturns = 1;
		}
		if (ceiling < 0) {
			ceiling = 0;
		}
		ArrayList<PersonNameMatcher> results = new ArrayList<PersonNameMatcher>(
				0);

		Calendar personDOB = person.getDateOfBirth();
		String personLast = person.getLastName();
		String personFirst = person.getFirstName();
		String personGender = person.getGender();
		for (PersonNameMatcher candidate : sourcePersons) {
			String candidateLast = candidate.getLastName();
			String candidateFirst = candidate.getFirstName();
			String candidateGender = candidate.getGender();
			Calendar candidateDOB = candidate.getDateOfBirth();

			int lastDiff = StringUtils.getLevenshteinDistance(candidateLast,
					personLast);
			int firstDiff = StringUtils.getLevenshteinDistance(candidateFirst,
					personFirst);
			int confidence = firstDiff;

			if (lastDiff != 0) {
				int[] diffs = new int[] { lastDiff };
				int altDiff;
				int hyphenated;
				if (StringUtils.isNotEmpty(alternateLastName)) {
					altDiff = StringUtils.getLevenshteinDistance(candidateLast,
							alternateLastName);
					diffs = ArrayUtils.add(diffs, altDiff);
					hyphenated = checkForHyphenated(alternateLastName,
							candidateLast);
					if (hyphenated > -1) {
						diffs = ArrayUtils.add(diffs, hyphenated);
					}
				}
				hyphenated = checkForHyphenated(personLast, candidateLast);

				if (hyphenated > -1) {
					diffs = ArrayUtils.add(diffs, hyphenated);
				}

				confidence = confidence + NumberUtils.min(diffs);
			}

			boolean genderEquals = StringUtils.equals(candidateGender,
					personGender);
			boolean dobEquals = DateUtils.truncatedEquals(candidateDOB,
					personDOB, Calendar.DATE);
			boolean dobYearEquals = DateUtils.truncatedEquals(candidateDOB,
					personDOB, Calendar.YEAR);
			boolean weakMatchEquals = person.weakMatch(candidate);

			if (dobEquals && genderEquals) {
				tempResults.add(new PersonConfidence(candidate, confidence));
				continue;
			}
			confidence = confidence + confidentIncrement;

			// Year Difference
			if (candidateDOB.get(Calendar.MONTH) == personDOB
					.get(Calendar.MONTH)
					&& candidateDOB.get(Calendar.DATE) == personDOB
							.get(Calendar.DATE)
					&& confidence < yearOfBirthThreshold) {
				tempResults.add(new PersonConfidence(candidate, confidence));
				continue;
			}
			confidence = confidence + confidentIncrement;

			// Month off or transposed days (e.g. 1/21/yyyy 1/12/yyyy)
			if (dobYearEquals && confidence < dayOfBirthThreshold) {
				if (candidateDOB.get(Calendar.DATE) == personDOB
						.get(Calendar.DATE)) {
					tempResults
							.add(new PersonConfidence(candidate, confidence));
					continue;
				}
				confidence = confidence + confidentIncrement;
				String candidateDay = String.format("%02d",
						candidateDOB.get(Calendar.DAY_OF_MONTH));
				String personDay = String.format("%02d",
						personDOB.get(Calendar.DAY_OF_MONTH));

				candidateDay = StringUtils.reverse(candidateDay);

				if (StringUtils.equals(candidateDay, personDay)) {
					tempResults.add(new PersonConfidence(candidate, confidence
							+ confidentIncrement));
					continue;
				}
			}
			confidence = confidence + confidentIncrement;

			if (weakMatchEquals && confidence < weakMatchThreshold) {
				tempResults.add(new PersonConfidence(candidate, confidence
						+ confidentIncrement));
				continue;
			}
			confidence = confidence + confidentIncrement;

			tempResults.add(new PersonConfidence(candidate, confidence));

		}

		Collections.sort(tempResults);
		if (tempResults.size() > 0) {
			if (tempResults.size() < maxReturns) {
				maxReturns = tempResults.size();
			}
			for (int i = 0; i < maxReturns; i++) {
				PersonConfidence m = tempResults.get(i);
				if (m.getConfidence() <= ceiling) {
					results.add(m.getPerson());
				}
			}
		}

		return results;
	}

	private int checkForHyphenated(String personLast, String candidateLast) {

		if (StringUtils.contains(personLast, "-")
				|| StringUtils.contains(candidateLast, "-")
				|| StringUtils.contains(personLast, " ")
				|| StringUtils.contains(candidateLast, " ")) {
			// Check for mixed up or missing hyphenated names

			ArrayList<String> memberParts = new ArrayList<String>(0);
			String[] parts = StringUtils.split(candidateLast, '-');
			for (int i = 0; i < parts.length; i++) {
				memberParts.add(parts[i]);
			}
			parts = StringUtils.split(candidateLast, ' ');
			for (int i = 0; i < parts.length; i++) {
				memberParts.add(parts[i]);
			}

			ArrayList<String> claimParts = new ArrayList<String>(0);
			parts = StringUtils.split(personLast, '-');
			for (int i = 0; i < parts.length; i++) {
				claimParts.add(parts[i]);
			}
			parts = StringUtils.split(personLast, ' ');
			for (int i = 0; i < parts.length; i++) {
				claimParts.add(parts[i]);
			}

			ArrayList<Integer> lastNameConfidences = new ArrayList<Integer>(0);

			for (String claimPart : claimParts) {
				for (String memberPart : memberParts) {
					lastNameConfidences.add(StringUtils.getLevenshteinDistance(
							claimPart, memberPart));
				}
			}

			int[] conf = ArrayUtils.toPrimitive(lastNameConfidences
					.toArray(new Integer[0]));

			return NumberUtils.min(conf);

		}

		return -1;
	}

}