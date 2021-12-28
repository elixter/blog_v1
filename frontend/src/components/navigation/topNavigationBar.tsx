import React from 'react';
import useAuthentication, { UserType } from '../authentication/hooks/useAuthentication';
import AuthNavigation from './authNavigation/authNavigation';
import UserNavigation from './userNavigation/userNavigation';

const TopNavigationBar = function () {
	const { user } = useAuthentication();

	// eslint-disable-next-line react/jsx-no-useless-fragment
	return <>{user && (user.type === UserType.Login ? <UserNavigation user={user} /> : <AuthNavigation />)}</>;
};

export default TopNavigationBar;
