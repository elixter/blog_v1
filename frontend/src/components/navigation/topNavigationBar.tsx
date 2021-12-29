import React, { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import useAuthentication, { UserType } from '../authentication/hooks/useAuthentication';
import AuthNavigation from './authNavigation/authNavigation';
import UserNavigation from './userNavigation/userNavigation';
import Logo from './logo/logo';

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
	}, [onScroll, top]);

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
						project
					</Link>
					<Link to="/chat" className="menu-tit">
						chat
					</Link>
				</div>
			</div>
			<div className="hd-right">
				{user && (user.type === UserType.Login ? <UserNavigation user={user} /> : <AuthNavigation />)}
			</div>
		</div>
	);
};

export default TopNavigationBar;
