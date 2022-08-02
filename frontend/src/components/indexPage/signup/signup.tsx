import { ChangeEvent, useCallback, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { Navigation } from '@mui/icons-material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEye, faEyeSlash } from '@fortawesome/free-regular-svg-icons';
import { StaticPath } from '../../pagePath/pagePath';
import useSignUp from '../../hooks/auth/useSignup';
import icoWarning1 from '../../../static/img/ico_warning1.png';

const passwordValidationRegExp = '^(?=.*\\d)(?=.*[a-zA-Z])(?=.*([^\\w\\d\\s]|_)).{8,72}$';

const Signup = function () {
	const [inputs, setInputs] = useState({
		id: '',
		password: '',
		name: '',
		email: '',
		validatePassword: '',
	});
	const [visiblePassword, setVisiblePassword] = useState(false);
	const [passwordValidation, setPasswordValidation] = useState(false);
	const [confirmPasswordValidation, setConfirmPasswordValidation] = useState(false);
	const [idValidation, setIdValidation] = useState<boolean>(false);
	const [idValidated, setIdValidated] = useState<boolean>(false);
	const history = useHistory();
	const { fetch, loading } = useSignUp();

	const onChange = useCallback(
		(e: ChangeEvent<HTMLInputElement>) => {
			const { name, value } = e.target;
			setInputs({
				...inputs,
				[name]: value,
			});
			if (inputs.id === '') {
				setIdValidated(false);
			}

			if (name === 'password') {
				setPasswordValidation(new RegExp(passwordValidationRegExp).test(value));
				setConfirmPasswordValidation(inputs.validatePassword === value);
			}

			if (name === 'validatePassword') {
				setConfirmPasswordValidation(inputs.password === value);
			}
		},
		[inputs, setInputs]
	);

	const onToggleVisiblePassword = () => {
		setVisiblePassword(!visiblePassword);
	};
	const submit = async () => {
		await fetch({
			name: inputs.name,
			loginId: inputs.id,
			loginPw: inputs.password,
			email: inputs.email,
			passwordValidation,
			confirmPasswordValidation,
		}).then((res) => {
			if (!res) return;
			history.push(StaticPath.MAIN);
		});
	};

	const onIdValidate = useCallback(() => {
		console.log(inputs.id);
		setIdValidated(true);
	}, [inputs.id]);

	return (
		<div id="container">
			{loading}
			<div className="pos">
				<div className="box-member">
					<div className="tit-page">계정만들기</div>

					<ol className="list-frm">
						<li>
							<input type="text" name="name" placeholder="이름을 입력하세요" className="inp-frm" onChange={onChange} />
						</li>
						<li>
							<input
								type="text"
								name="email"
								placeholder="이메일을 입력하세요"
								className="inp-frm"
								onChange={onChange}
							/>
						</li>
						<li>
							<div className="inp-group">
								<input
									type="text"
									name="id"
									placeholder="아이디를 입력하세요"
									className="inp-frm"
									onChange={onChange}
								/>
							</div>
							<div>
								<button type="button" className="btn-link id-validate" onClick={onIdValidate}>
									중복확인
								</button>
								{!idValidation && inputs.id !== '' && idValidated && (
									<div className="txt">
										<img src={icoWarning1} alt=" " />
										사용중인 아이디입니다.
									</div>
								)}
							</div>
						</li>
						<li>
							<div className="inp-group">
								<input
									placeholder="비밀번호 (영문, 숫자, 특수문자 8-30자)"
									className="inp-frm2"
									name="password"
									type={visiblePassword ? '' : 'password'}
									onChange={(e) => {
										onChange(e);
									}}
								/>
								<button type="button" onClick={onToggleVisiblePassword} className="btn-view js-view">
									<FontAwesomeIcon icon={!visiblePassword ? faEyeSlash : faEye} />
								</button>
							</div>
							{!passwordValidation && (
								<div className="txt">
									<img src={icoWarning1} alt=" " />
									반드시 기호, 영문, 숫자를 8자 이상 조합하여야 합니다
								</div>
							)}
						</li>
						<li>
							<div className="inp-group">
								<input
									placeholder="비밀번호 확인"
									className="inp-frm2"
									name="validatePassword"
									type={visiblePassword ? '' : 'password'}
									onChange={onChange}
								/>
							</div>
							{!confirmPasswordValidation && (
								<div className="txt">
									<img src={icoWarning1} alt=" " />
									비밀번호가 틀립니다.
								</div>
							)}
						</li>
					</ol>

					<input type="button" value="회원가입" className="btn-frm" onClick={submit} />
				</div>
			</div>
		</div>
	);
};

export default Signup;
