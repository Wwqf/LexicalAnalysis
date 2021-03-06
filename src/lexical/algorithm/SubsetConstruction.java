package lexical.algorithm;

import lexical.diagram.unit.State;
import lexical.diagram.unit.StateType;
import lexical.diagram.unit.TransitionFunc;
import lexical.global.GlobalMark;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 子集构造法, NFA转DFA
 */
public class SubsetConstruction {

	/**
	 * 能够从stateSets中某个状态state出发通过标号为ε的转换到达的NFA状态集合
	 * @param stateSets
	 * @return
	 */
	public Set<State> epsilon_closure(Set<State> stateSets) {
		Set<State> result = new HashSet<>();

		for (State state : stateSets) {
			result.addAll(epsilon_closure(state));
		}

		return result;
	}

	/**
	 * 能够从NFA的状态state开始只通过ε转换到达的NFA状态集合
	 * @param state
	 * @return
	 */
	public Set<State> epsilon_closure(State state) {
		Set<State> result = new HashSet<>();
		// 如果当前状态是开始状态，则将当前状态加入到集合中
		if (state.getType() == StateType.START_STATE) {
			result.add(state);
		}

		Set<State> rec = recursiveQuery(new HashSet<>(), state, GlobalMark.Epsilon);
		if (rec != null) {
			result.addAll(rec);
		}
		return result;
	}

	/**
	 * 能够从stateSets中某个状态state出发通过标号为matchCharacter的转换到达的NFA状态集合
	 * @param stateSets
	 * @param matchCharacter
	 * @return
	 */
	public Set<State> move(Set<State> stateSets, char matchCharacter) {
		Set<State> result = new HashSet<>();

		for (State state : stateSets) {
			Set<State> rec = recursiveQuery(new HashSet<>(), state, matchCharacter);
			if (rec != null) {
				result.addAll(rec);
			}
		}

		return result;
	}

	/**
	 * 因为是用规则的方式匹配，存在连词符规则[a-z]....
	 * 连词符规则是多个字符，无法用单字符move匹配，所以用增加一个特殊的move方法
	 * @param stateSets
	 * @param matchString
	 * @return
	 */
	@Deprecated
	public Set<State> move(Set<State> stateSets, String matchString) {
		if (matchString.length() == 1) return move(stateSets, matchString.charAt(0));

		Set<State> result = new HashSet<>();

		for (State state : stateSets) {
			Set<State> rec = recursiveQuery(new HashSet<>(), state, matchString);
			if (rec != null) {
				result.addAll(rec);
			}
		}

		return result;
	}

	/**
	 * 状态能通过字符matchChar到达的状态集合
	 * @param querySets 检测集合
	 * @param state 状态
	 * @param matchChar
	 * @return
	 */
	private Set<State> recursiveQuery(Set<State> querySets, State state, char matchChar) {
		/*
		 *  如果集合中存在当前状态，则代表已经查询过，直接返回null，否则添加当前状态到集合中.
		 */
		if (querySets.contains(state)) return null;
		querySets.add(state);

		Set<State> result = new HashSet<>();
		List<TransitionFunc> lists = state.getTransitionFuncList();

		/* 遍历当前状态的转换函数集 */
		for (TransitionFunc item : lists) {
			// 如果能通过matchChar到下一个状态，则将下一个状态加入节点，并且递归调用查询下一个状态的ε集
			if (item.match(matchChar)) {
				result.add(item.getNextState());
				Set<State> rec = recursiveQuery(querySets, item.getNextState(), matchChar);
				if (rec != null) {
					result.addAll(rec);
				}
			}
		}

		return result;
	}

	/**
	 * 状态能通过字符matchChar到达的状态集合
	 * @param querySets 检测集合
	 * @param state 状态
	 * @param matchString
	 * @return
	 */
	@Deprecated
	private Set<State> recursiveQuery(Set<State> querySets, State state, String matchString) {
		/*
		 *  如果集合中存在当前状态，则代表已经查询过，直接返回null，否则添加当前状态到集合中.
		 */
		if (querySets.contains(state)) return null;
		querySets.add(state);

		Set<State> result = new HashSet<>();
		List<TransitionFunc> lists = state.getTransitionFuncList();

		/* 遍历当前状态的转换函数集 */
		for (TransitionFunc item : lists) {
			// 调用此方法的只有连词符规则, 所以只做特殊处理
//			if (item.match(matchChar)) {
//				result.add(item.getNextState());
//				Set<State> rec = recursiveQuery(querySets, item.getNextState(), matchChar);
//				if (rec != null) {
//					result.addAll(rec);
//				}
//			}
		}

		return result;
	}
}
