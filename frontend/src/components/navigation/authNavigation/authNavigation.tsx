import React from 'react';
import { Link } from 'react-router-dom';
import Logo from '../logo/logo';

const AuthNavigation = function () {
	return (
		<div className="hd-login-before">
			<Link to="/login">로그인</Link>
			&#10072;
			<Link to="/signup">회원가입</Link>
		</div>
	);
};

export default AuthNavigation;