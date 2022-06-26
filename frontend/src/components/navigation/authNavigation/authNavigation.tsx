import React, { useCallback, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import Modal from '../../utils/modal';
import useLogin from '../../hooks/auth/userLogin';
import { LoginParams } from '../../authentication/hooks/types';
import useAuthentication from '../../hooks/auth/useAuthentication';
import { StaticPath } from '../../pagePath/pagePath';
import Login from "./login";

const AuthNavigation = function () {
	const [loginOpen, setLoginOpen] = useState(false);

	const onClickLogin = useCallback(() => {
		setLoginOpen(true);
	}, []);

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
				<Login />
			</Modal>
		</>
	);
};

export default AuthNavigation;
