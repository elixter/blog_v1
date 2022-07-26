import { Link, useHistory } from 'react-router-dom';
import React, { useCallback, useState } from 'react';
import useLogin from '../../hooks/auth/userLogin';
import useAuthentication from '../../hooks/auth/useAuthentication';
import { LoginParams } from '../../authentication/hooks/types';
import { StaticPath } from '../../pagePath/pagePath';

const Index = function () {
	const { fetch, loading } = useLogin();
	const { mutate } = useAuthentication();
	const history = useHistory();

	const [inputs, setInputs] = useState({
		id: '',
		pw: '',
	});

	const login = useCallback(
		async (params: LoginParams) => {
			const response = await fetch(params);
			if (response) {
				await mutate();
				history.push(StaticPath.MAIN);
			}
		},
		[fetch, history, mutate]
	);

	const onChange = useCallback(
		(e) => {
			const { name, value } = e.target;
			setInputs({
				...inputs,
				[name]: value,
			});
		},
		[inputs, setInputs]
	);

	const onPressEnter = useCallback(
		(e) => {
			if (e.key === 'Enter') login(inputs);
		},
		[inputs, login]
	);

	return (
		<div className="box-member" onKeyDown={onPressEnter}>
			<ol className="list-frm">
				<li>
					<input type="text" name="id" placeholder="아이디를 입력하세요" className="inp-frm" onChange={onChange} />
				</li>
				<li>
					<input
						type="password"
						name="pw"
						placeholder="비밀번호를 입력하세요"
						className="inp-frm"
						onChange={onChange}
					/>
				</li>
			</ol>

			<div className="util">
				<Link to="/signup" className="btn-link">
					회원가입
				</Link>
			</div>

			<input
				type="button"
				value="로그인"
				className="btn-frm"
				onClick={() => {
					login(inputs);
				}}
			/>
		</div>
	);
};

export default Index;
