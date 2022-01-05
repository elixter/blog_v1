import React, { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { makeStyles } from '@mui/styles';
import { User } from '../../authentication/hooks/useAuthentication';
import Logo from '../logo/logo';
import DropMenu from '../../utils/dropMenu/dropMenu';
import { StaticPath } from '../../pagePath/pagePath';

const styles = makeStyles({
	profileImage: {
		width: '30px',
		height: '30px',
		borderRadius: '50%',
		overflow: 'hidden',
		cursor: 'pointer',
	},
});

type Props = {
	user: User;
};

type profileImageProps = {
	src: string;
};

const ProfileImage = function ({ src }: profileImageProps) {
	const useStyle = styles();

	return <img alt="" className={`img-profile ${useStyle.profileImage}`} src={src} />;
};

const UserNavigation = function ({ user }: Props) {
	const [flag, setFlag] = useState(false);

	const toggleMenu = useCallback(
		(e) => {
			e.nativeEvent.stopImmediatePropagation();
			setFlag(!flag);
		},
		[flag, setFlag]
	);

	const closeMenu = useCallback(
		(e) => {
			setFlag(false);
		},
		[setFlag]
	);

	useEffect(() => {
		document.addEventListener('click', closeMenu);
		return () => {
			document.removeEventListener('click', closeMenu);
		};
	}, [closeMenu]);

	return (
		<div className="hd-profile" onClick={toggleMenu} tabIndex={0} role="button" onKeyDown={toggleMenu}>
			<div className="user-info js-more">
				<ProfileImage src={user?.profile?.profileImage?.url || ''} />
				<div className="user-id">{user?.profile?.name} 님</div>
			</div>

			<DropMenu open={flag}>
				<Link to={StaticPath.PROFILE}>내 정보</Link>
				<button
					type="button"
					style={{
						width: '100%',
					}}
					// onClick={async () => {
					// 	fetch();
					// }}
				>
					로그아웃
				</button>
			</DropMenu>
		</div>
	);
};

export default UserNavigation;
