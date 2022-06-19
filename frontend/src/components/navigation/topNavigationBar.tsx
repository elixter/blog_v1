import React, { memo, useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import AuthNavigation from './authNavigation/authNavigation';
import Logo from './logo/logo';
import useAuthentication, { UserType } from '../hooks/auth/useAuthentication';
import UserNavigation from './userNavigation/userNavigation';

const TopNavigationBar = function () {
	const { user } = useAuthentication();
	const [top, setTop] = useState(true);
	const onScroll = useCallback(() => {
		if (document.documentElement.scrollTop > 0) {
			setTop(false);
		} else {
			setTop(true);
		}
	}, []);
	useEffect(() => {
		window.addEventListener('scroll', onScroll);
		console.log(user);
	}, [onScroll, top, user]);

	return (
		<div className={`header ${top ? 'top' : 'non-top'}`}>
			<div className="hd-left">
				<Logo />
			</div>
			<div className="hd-center">
				<div className="hd-menu">
					<Link to="/about" className="menu-tit">
						About
					</Link>
					<Link to="/blog" className="menu-tit">
						Blog
					</Link>
					<Link to="/project" className="menu-tit">
						Project
					</Link>
					<Link to="/chat" className="menu-tit">
						Contact
					</Link>
				</div>
			</div>
			<div className="hd-right">
				{user && (user.type === UserType.Login ? <UserNavigation user={user} /> : <AuthNavigation />)}
			</div>
		</div>
	);
};

export default memo(TopNavigationBar);
