import React from 'react';
import Logo from './logo/logo';

const TopNavigationBar = function () {
	return (
		<div className="header">
			<Logo />
			<div className="auth">
				<button type="button">로그인</button>
				<button type="button">회원가입</button>
			</div>
		</div>
	);
};

export default TopNavigationBar;
