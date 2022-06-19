import React, { useCallback, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import Modal from '../../utils/modal';
import useLogin from '../../hooks/auth/userLogin';
import { LoginParams } from '../../api/auth/types';
import useAuthentication from '../../hooks/auth/useAuthentication';
import { StaticPath } from '../../pagePath/pagePath';

const AuthNavigation = function () {
	const { fetch, loading } = useLogin();
	const { mutate } = useAuthentication();
	const history = useHistory();
	const [loginOpen, setLoginOpen] = useState(false);
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

	const onClickLogin = useCallback(() => {
		setLoginOpen(true);
	}, []);

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
		<>
			<div className="hd-login-before">
				<Link to="" onClick={onClickLogin}>
					로그인
				</Link>
				&#10072;
				<Link to="">회원가입</Link>
			</div>
			<Modal head="로그인" setIsOpen={setLoginOpen} isOpen={loginOpen}>
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
			</Modal>
		</>
	);
};

export default AuthNavigation;
